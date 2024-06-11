import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    rootNode = NodeOperations.createTopLevelScript(
        "function foo() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    var localDiv = goog.dom.createElement(DIV);" +
        "  });" +
        "  goog.scope(function() {" +
        "    var localDom = goog.dom;" +
        "    var DIV = localDom.TagName.DIV;" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "  goog.scope(function() {" +
        "    this.localDom = goog.dom;" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  });" +
        "  goog.scope(function() {" +
        "    throw new Error('Test error');" +
        "  });" +
        "  goog.scope(function() {" +
        "    return 42;" +
        "  });" +
        "  goog.scope(function() {" +
        "    function test() {" +
        "      return 42;" +
        "    }" +
        "    return test;" +
        "  });" +
        "  goog.scope(function() {" +
        "    var x = goog.x;" +
        "    x = 42;" +
        "  });" +
        "  goog.scope(function() {" +
        "    var goog = {dom: {createElement: function() {}}};" +
        "  });" +
        "  goog.scope(function() {" +
        "    var goog = {dom: {TagName: {DIV: null}}};" +
        "  });" +
        "}"
    );
  }

  @Test
  public void testValidScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    scopedAliases.process(null, rootNode);
    assertEquals(5, rootNode.getChildCount());
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeUsedImproperly() {
    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    Node badCall = NodeOperations.getChildAt(rootNode, 6);
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    assertEquals(7, rootNode.getChildCount());
    Node badCallParent = badCall.getParent();
    Node Utils = NodeOperations.getChildAt(rootNode, 3);
    Node googScope = NodeOperations.getChildAt(Utils, 2);
    Node badCallAfterScope = NodeOperations.getChildAt(badCallParent, 1);
    NodeUtilities.tryMergeBlock(badCall);
    assertEquals(8, rootNode.getChildCount());
    Node newBadCall = NodeOperations.getChildAt(rootNode, 7);
    scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    Node googScopeNew = NodeOperations.getChildAt(Utils, 2);
    assertNotEquals(googScope, googScopeNew);
    Node badCallNew = NodeOperations.getChildAt(newBadCall, 1);
    assertFalse(badCallAfterScope.isSameNode(badCallNew.getParent()));
    throw new AssertionError("Unexpected behavior.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeHasBadParameters() {
    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    Node badCall = NodeOperations.getChildAt(rootNode, 7);
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    assertEquals(8, rootNode.getChildCount());
    Node newBadCall = NodeOperations.getChildAt(rootNode, 7);
    scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    Node googScopeNew = NodeOperations.getChildAt(Utils, 2);
    assertNotEquals(googScope, googScopeNew);
    Node newBadCallParent = newBadCall.getParent();
    Node newBadCallParam = NodeOperations.getChildAt(newBadCall, 1);
    Node newBadCallFunction = NodeOperations.getChildAt(newBadCall, 0);
    assertFalse(newBadCallFunction.isFunction());
    throw new AssertionError("Unexpected behavior.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeRefersToThis() {
    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    Node badCall = NodeOperations.getChildAt(rootNode, 8);
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    assertEquals(9, rootNode.getChildCount());
    Node newBadCall = NodeOperations.getChildAt(rootNode, 8);
    scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    Node googScopeNew = NodeOperations.getChildAt(Utils, 2);
    assertNotEquals(googScope, googScopeNew);
    Node newBadCallThis = NodeOperations.getChildAt(newBadCall, 1);
    assertNotNull(newBadCallThis.getParent().getJsDocInfo());
    JSDocInfo info = newBadCallThis.getParent().getJsDocInfo();
    assertNotNull(info);
    String[] expected = {"this"};
    String[] actual = info.getNames();
    assertArrayEquals(expected, actual);
    throw new AssertionError("Unexpected behavior.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeUsesReturn() {
    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    Node badCall = NodeOperations.getChildAt(rootNode, 9);
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    assertEquals(10, rootNode.getChildCount());
    Node newBadCall = NodeOperations.getChildAt(rootNode, 9);
    scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    Node googScopeNew = NodeOperations.getChildAt(Utils, 2);
    assertNotEquals(googScope, googScopeNew);
    Node newBadCallReturn = NodeOperations.getChildAt(newBadCall, 0);
    assertTrue(newBadCallReturn.isReturnStatement());
    throw new AssertionError("Unexpected behavior.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeUsesThrow() {
    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    Node badCall = NodeOperations.getChildAt(rootNode, 10);
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    assertEquals(11, rootNode.getChildCount());
    Node newBadCall = NodeOperations.getChildAt(rootNode, 10);
    scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    Node googScopeNew = NodeOperations.getChildAt(Utils, 2);
    assertNotEquals(googScope, googScopeNew);
    Node newBadCallThrow = NodeOperations.getChildAt(newBadCall, 0);
    assertTrue(newBadCallThrow.isThrowStatement());
    throw new AssertionError("Unexpected behavior.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeAliasRedefined() {
    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    Node badCall = NodeOperations.getChildAt(rootNode, 11);
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    assertEquals(12, rootNode.getChildCount());
    Node newBadCall = NodeOperations.getChildAt(rootNode, 11);
    scopedAliases = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition pos) {
            return new AliasTransformation() {};
          }
        });
    scopedAliases.process(null, rootNode);
    NodeUtilities.tryApplyTransformation(scopedAliases.transformation, rootNode);
    Node googScopeNew = NodeOperations.getChildAt(Utils, 2);
    assertNotEquals(googScope, googScopeNew);
    Node newBadCallLocal = NodeOperations.getChildAt(newBadCall, 0);
    Node newBadCallLocalInitialValue = newBadCallLocal.getInitialValue();
    Node newBadCallLocalIdentifier = NodeOperations.getChildAt(newBadCallLocal, 0);
    String newBadCallLocalName = newBadCallLocalIdentifier.getString();
    Node oldBadCall = NodeOperations.getChildAt(rootNode, 11);
    Node oldBadCallLocal = NodeOperations.getChildAt(oldBadCall, 0);
    Node oldBadCallLocalInitialValue = oldBadCallLocal.getInitialValue();
    Node oldBadCallLocalIdentifier = NodeOperations.getChildAt(oldBadCallLocal, 0);
    String oldBadCallLocalName = oldBadCallLocalIdentifier.getString();
    assertNotEquals(newBadCallLocalName, oldBadCallLocalName);
    throw new AssertionError("Unexpected behavior.");
  }
}