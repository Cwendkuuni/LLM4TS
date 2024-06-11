import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for CheckGlobalThis class
 */
public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_Function() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);

    assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal() {
    }, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverse_Function_Constructor() {
    Node functionNode = new Node(Token.FUNCTION);
    Node constructorAnnotation = new Node(Token.ANNOTATION, "constructor");
    functionNode.addChildToEnd(constructorAnnotation);
    Node parentNode = new Node(Token.SCRIPT);

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal() {
    }, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverse_Function_ThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    Node thisAnnotation = new Node(Token.ANNOTATION, "@this");
    functionNode.addChildToEnd(thisAnnotation);
    Node parentNode = new Node(Token.SCRIPT);

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal() {
    }, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverse_Function_Interface() {
    Node functionNode = new Node(Token.FUNCTION);
    Node interfaceAnnotation = new Node(Token.ANNOTATION, "interface");
    functionNode.addChildToEnd(interfaceAnnotation);
    Node parentNode = new Node(Token.SCRIPT);

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal() {
    }, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverse_Function_Override() {
    Node functionNode = new Node(Token.FUNCTION);
    Node overrideAnnotation = new Node(Token.ANNOTATION, "override");
    functionNode.addChildToEnd(overrideAnnotation);
    Node parentNode = new Node(Token.SCRIPT);

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal() {
    }, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverse_Function_PrototypeProperty() {
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeProperty = new Node(Token.GETPROP, null, "prototype");
    Node parentNode = new Node(Token.NAME, "prototype");

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal() {
    }, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverse_Function_PrototypeSubProperty() {
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeProperty = new Node(Token.GETPROP, null, "prototype");
    Node prototypeSubProperty = new Node(Token.GETPROP, null, "subProperty");
    Node parentNode = new Node(Token.NAME, "prototype");

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal() {
    }, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverse_Function_LentToPrototype() {
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeProperty = new Node(Token.GETPROP, null, "prototype");
    Node assignmentNode = new Node(Token.ASSIGN, null, functionNode);
    Node parentNode = new Node(Token.NAME, "prototype");

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal() {
    }, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverse_Function_AssignedToVariable() {
    Node assignNode = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"));
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);

    Node aVariable = assignNode.getFirstChild();
    aVariable.addChildToEnd(functionNode);

    assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal() {
    }, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverse_Function_AssignedToObject() {
    Node assignNode = new Node(Token.ASSIGN, null, new Node(Token.OBJECTLITERAL));
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);

    Node objectLiteral = assignNode.getFirstChild();
    Node functionInObject = new Node(Token.ASSIGN, null, functionNode);
    objectLiteral.addChildToEnd(functionInObject);

    assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal() {
    }, functionNode, parentNode));
  }

  @Test
  public void testShouldReportThis_Function() {
    Node thisNode = new Node(Token.THIS);
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);

    when(compiler.report(any(NodeTraversal.class), eq(checkLevel(Warning)), any(DiagnosticType.class)))
        .thenReturn(mock(Node.class));

    checkGlobalThis.visit(new NodeTraversal() {
    }, thisNode, functionNode);
    checkGlobalThis.visit(new NodeTraversal() {
    }, functionNode, parentNode);

    assertTrue(checkGlobalThis.shouldReportThis(thisNode, functionNode));
  }

  @Test
  public void testShouldReportThis_Function_PropertyAccess() {
    Node thisNode = new Node(Token.THIS);
    Node parentNode = new Node(Token.GETPROP, null, "property");
    Node functionNode = new Node(Token.FUNCTION);

    when(compiler.report(any(NodeTraversal.class), eq(checkLevel(Warning)), any(DiagnosticType.class)))
        .thenReturn(mock(Node.class));

    checkGlobalThis.visit(new NodeTraversal() {
    }, thisNode, functionNode);
    checkGlobalThis.visit(new NodeTraversal() {
    }, parentNode, functionNode);

    assertTrue(checkGlobalThis.shouldReportThis(thisNode, parentNode));
  }

  @Test
  public void testShouldNotReportThis_Function_Prototype() {
    Node thisNode = new Node(Token.THIS);
    Node prototypeNode = new Node(Token.GETPROP, null, "prototype");
    Node functionNode = new Node(Token.FUNCTION);

    when(compiler.report(any(NodeTraversal.class), eq(checkLevel(Warning)), any(DiagnosticType.class)))
        .thenReturn(mock(Node.class));

    checkGlobalThis.visit(new NodeTraversal() {
    }, thisNode, functionNode);
    checkGlobalThis.visit(new NodeTraversal() {
    }, prototypeNode, functionNode);

    assertFalse(checkGlobalThis.shouldReportThis(thisNode, prototypeNode));
  }

  @Test
  public void testShouldReportThis_Function_PrototypeProperty() {
    Node thisNode = new Node(Token.THIS);
    Node prototypePropertyNode = new Node(Token.GETPROP, null, "PrototypeProperty");
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeNode = new Node(Token.NAME, "Prototype");

    when(compiler.report(any(NodeTraversal.class), eq(checkLevel(Warning)), any(DiagnosticType.class)))
        .thenReturn(mock(Node.class));

    checkGlobalThis.visit(new NodeTraversal() {
    }, thisNode, functionNode);
    checkGlobalThis.visit(new NodeTraversal() {
    }, prototypePropertyNode, prototypeNode);

    assertFalse(checkGlobalThis.shouldReportThis(thisNode, prototypePropertyNode));
  }

  @Test
  public void testShouldNotReportThis_Function_PrototypeSubProperty() {
    Node thisNode = new Node(Token.THIS);
    Node prototypePropertyNode = new Node(Token.GETPROP, null, "PrototypeProperty");
    Node prototypeSubPropertyNode = new Node(Token.GETPROP, null, "SubProperty");
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeNode = new Node(Token.NAME, "Prototype");

    when(compiler.report(any(NodeTraversal.class), eq(checkLevel(Warning)), any(DiagnosticType.class)))
        .thenReturn(mock(Node.class));

    checkGlobalThis.visit(new NodeTraversal() {
    }, thisNode, functionNode);
    checkGlobalThis.visit(new NodeTraversal() {
    }, prototypePropertyNode, prototypeNode);
    checkGlobalThis.visit(new NodeTraversal() {
    }, prototypeSubPropertyNode, prototypeNode);

    assertFalse(checkGlobalThis.shouldReportThis(thisNode, prototypeSubPropertyNode));
  }

  private AbstractCompiler.CheckLevel checkLevel(AbstractCompiler.CheckLevel level) {
    return level;
  }
}