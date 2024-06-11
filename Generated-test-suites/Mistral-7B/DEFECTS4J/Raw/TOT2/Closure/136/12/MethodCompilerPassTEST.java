import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.MethodCompilerPass.*;
import com.google.javascript.rhino.*;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

  @ org.junit.Before
  public void setUp() {
    // Set up your test environment here, if necessary
  }

  @ org.junit.Test
  public void testGetExternMethods() {
    Node externs = NodeOperations.createNodeFromString(
        "Object.prototype.foo = function() {};\n" +
        "function foo() {}");

    MethodCompilerPass mcp = new MethodCompilerPass(new Compiler()) {
      @Override
      Callback getActingCallback() {
        fail("Should not be called in this test");
        return null;
      }

      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          Multimap<String, Node> signatures = HashMultimap.create();

          @Override
          public void reset() {
            signatures.clear();
          }

          @Override
          public void addSignature(String functionName, Node functionNode, String sourceFile) {
            signatures.put(functionName, functionNode);
          }

          @Override
          public void removeSignature(String functionName) {
            signatures.remove(functionName);
          }

          public Node getFunctionByName(String functionName) {
            return signatures.get(functionName).get(0);
          }
        };
      }
    };

    mcp.process(externs, null);

    // Test that extern methods are properly collected
    assertEquals(2, mcp.externMethods.size());
    assertEquals("Object.prototype.foo", mcp.externMethods.iterator().next());
    assertEquals("foo", mcp.externMethods.iterator().next());

    // Test that methods with signatures are stored correctly
    Node root = NodeOperations.createNodeFromString("function bar() {}");
    mcp.process(null, root);

    Node barFunction = mcp.getSignatureStore().getFunctionByName("bar");
    assertNotNull(barFunction);
    assertEquals(Token.FUNCTION, barFunction.getType());

    // Test that methods without signatures are ignored
    assertEquals(0, mcp.methodDefinitions.size());
  }

  @ org.junit.Test
  public void testGatherSignatures() {
    Node sourceCode = NodeOperations.createNodeFromString(
        "function Foo() {\n" +
        "  this.bar = function() {};\n" +
        "  this.baz = function(x) {};\n" +
        "}\n" +
        "Foo.prototype.quux = function() {};");

    MethodCompilerPass mcp = new MethodCompilerPass(new Compiler()) {
      @Override
      Callback getActingCallback() {
        fail("Should not be called in this test");
        return null;
      }

      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          Multimap<String, Node> signatures = HashMultimap.create();

          @Override
          public void reset() {
            signatures.clear();
          }

          @Override
          public void addSignature(String functionName, Node functionNode, String sourceFile) {
            signatures.put(functionName, functionNode);
          }

          @Override
          public void removeSignature(String functionName) {
            signatures.remove(functionName);
          }

          public Node getFunctionByName(String functionName) {
            return signatures.get(functionName).get(0);
          }
        };
      }
    };

    mcp.process(null, sourceCode);

    // Test that Foo.bar is correctly gathered
    Node fooBarFunction = mcp.getSignatureStore().getFunctionByName("Foo.bar");
    assertNotNull(fooBarFunction);
    assertEquals(Token.FUNCTION, fooBarFunction.getType());

    // Test that Foo.baz is correctly gathered
    Node fooBazFunction = mcp.getSignatureStore().getFunctionByName("Foo.baz");
    assertNotNull(fooBazFunction);
    assertEquals(Token.FUNCTION, fooBazFunction.getType());

    // Test that this.quux is correctly gathered
    Node quuxFunction = mcp.getFunctionByName("quux");
    assertNotNull(quuxFunction);
    assertEquals(Token.FUNCTION, quuxFunction.getType());
  }

  @ org.junit.Test
  public void testAddPossibleSignature() {
    Node sourceCode = NodeOperations.createNodeFromString(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}");

    MethodCompilerPass mcp = new MethodCompilerPass(new Compiler()) {
      @Override
      Callback getActingCallback() {
        fail("Should not be called in this test");
        return null;
      }

      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          Multimap<String, Node> signatures = HashMultimap.create();

          @Override
          public void reset() {
            signatures.clear();
          }

          @Override
          public void addSignature(String functionName, Node functionNode, String sourceFile) {
            signatures.put(functionName, functionNode);
          }

          @Override
          public void removeSignature(String functionName) {
            signatures.remove(functionName);
          }

          public Node getFunctionByName(String functionName) {
            return signatures.get(functionName).get(0);
          }
        };
      }
    };

    mcp.process(null, sourceCode);

    // Test that Foo.prototype.bar is correctly
    // added as a possible signature
    Node barFunction = mcp.getSignatureStore().getFunctionByName("bar");
    assertNotNull(barFunction);
    assertEquals(Token.FUNCTION, barFunction.getType());
  }

  @ org.junit.Test(expected = IllegalStateException.class)
  public void testGetFunctionByNameThrowsWhenFunctionIsNotAdded() {
    Node sourceCode = NodeOperations.createNodeFromString("function Foo() {}");

    MethodCompilerPass mcp = new MethodCompilerPass(new Compiler()) {
      @Override
      Callback getActingCallback() {
        fail("Should not be called in this test");
        return null;
      }

      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          Multimap<String, Node> signatures = HashMultimap.create();

          @Override
          public void reset() {
            signatures.clear();
          }

          @Override
          public void addSignature(String functionName, Node functionNode, String sourceFile) {
            signatures.put(functionName, functionNode);
          }

          @Override
          public void removeSignature(String functionName) {
            signatures.remove(functionName);
          }

          public Node getFunctionByName(String functionName) {
            return signatures.get(functionName).get(0);
          }
        };
      }
    };

    mcp.process(null, sourceCode);
    mcp.getFunctionByName("Foo.bar");
  }
}