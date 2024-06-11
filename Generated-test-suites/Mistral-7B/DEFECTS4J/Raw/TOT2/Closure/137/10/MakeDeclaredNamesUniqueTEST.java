import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.List;

public class MakeDeclaredNamesUniqueTest {

    @org.junit.Test
    public void testMakeDeclaredNamesUnique_simpleFunctionWithVariables() {
        Node ast = createAst("function foo(x, y) {" +
                "  var a = x;" +
                "  var b = y;" +
                "  return a + b;" +
                "}");

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal();
        nodeTraversal.traverse(ast, makeDeclaredNamesUnique);

        List<String> expectedDeclaredNames = Arrays.asList("a", "b", "x", "y");
        checkDeclaredNames(makeDeclaredNamesUnique, expectedDeclaredNames);
    }

    @org.junit.Test
    public void testMakeDeclaredNamesUnique_functionWithVariablesAndFunctionCalls() {
        Node ast = createAst("function foo(x, y) {" +
                "  var a = x;" +
                "  var b = y;" +
                "  function bar(z) {" +
                "    var c = z;" +
                "    return a + b + c;" +
                "  }" +
                "  return bar(5);" +
                "}");

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal();
        nodeTraversal.traverse(ast, makeDeclaredNamesUnique);

        List<String> expectedDeclaredNames = Arrays.asList("a", "b", "x", "y", "z", "c");
        checkDeclaredNames(makeDeclaredNamesUnique, expectedDeclaredNames);
    }

    @org.junit.Test
    public void testMakeDeclaredNamesUnique_functionWithNestedFunctions() {
        Node ast = createAst("function foo(x) {" +
                "  function bar(y) {" +
                "    var a = x;" +
                "    function baz() {" +
                "      var b = y;" +
                "      return a + b;" +
                "    }" +
                "    return baz();" +
                "  }" +
                "  return bar(5);" +
                "}");

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal();
        nodeTraversal.traverse(ast, makeDeclaredNamesUnique);

        List<String> expectedDeclaredNames = Arrays.asList("a", "x", "y", "b");
        checkDeclaredNames(makeDeclaredNamesUnique, expectedDeclaredNames);
    }

    @org.junit.Test
    public void testMakeDeclaredNamesUnique_functionWithVariableShadowing() {
        Node ast = createAst("function foo(x) {" +
                "  var a = x;" +
                "  function bar(x) {" +
                "    var a = x;" +
                "    return a;" +
                "  }" +
                "  return bar(5);" +
                "}");

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal();
        nodeTraversal.traverse(ast, makeDeclaredNamesUnique);

        List<String> expectedDeclaredNames = Arrays.asList("a", "x", "x");
        checkDeclaredNames(makeDeclaredNamesUnique, expectedDeclaredNames);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testMakeDeclaredNamesUnique_functionWithConflictingNames() {
        Node ast = createAst("function foo(x) {" +
                "  var x = 5;" +
                "  return x;" +
                "}");

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal();
        nodeTraversal.traverse(ast, makeDeclaredNamesUnique);

        // This test should fail with an AssertionError because the name "x"
        // cannot be uniquely renamed.
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testMakeDeclaredNamesUnique_functionWithFunctionNameConflict() {
        Node ast = createAst("function foo(x) {" +
                "  function foo() {" +
                "    return 5;" +
                "  }" +
                "  return foo();" +
                "}");

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal();
        nodeTraversal.traverse(ast, makeDeclaredNamesUnique);

        // This test should fail with an AssertionError because the function name
        // "foo" cannot be uniquely renamed within the same function.
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testMakeDeclaredNamesUnique_functionWithObjectPropertyConflict() {
        Node ast = createAst("var obj = {" +
                "  x: 1" +
                "};" +
                "function foo(x) {" +
                "  obj.x = x;" +
                "}");

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal();
        nodeTraversal.traverse(ast, makeDeclaredNamesUnique);

        // This test should fail with an AssertionError because the object
        // property "x" cannot be uniquely renamed.
    }

    private static Node createAst(String code) {
        TokenStream ts = new TokenStream(new StringReader(code));
        return ts.parseProgram();
    }

    private static void checkDeclaredNames(MakeDeclaredNamesUnique makeDeclaredNamesUnique,
                                         List<String> expectedDeclaredNames) {
        Map<String, String> actualDeclaredNames = makeDeclaredNamesUnique.nameMap;
        Iterator<Map.Entry<String, String>> iterator = actualDeclaredNames.entrySet().iterator();
        List<String> actualDeclaredNamesList = Lists.newArrayList(actualDeclaredNames.keySet());

        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String actualDeclaredName = next.getKey();
            String actualReplacementName = next.getValue();
            boolean expectedDeclaredNameExists = expectedDeclaredNames.contains(actualDeclaredName);
            boolean actualDeclaredNameExists = actualDeclaredNamesList.contains(actualDeclaredName);
            boolean expectedReplacementNameMatches =
                    (expectedDeclaredNameExists && actualReplacementName.equals(expectedDeclaredNames.get(0)));

            if (!expectedDeclaredNameExists || !expectedReplacementNameMatches ||
                    !actualDeclaredNameExists) {
                throw new AssertionError("Unexpected declared names: " + actualDeclaredNames);
            }

            expectedDeclaredNames.remove(actualDeclaredName);
        }

        if (!expectedDeclaredNames.isEmpty()) {
            throw new AssertionError("Missing declared names: " + expectedDeclaredNames);
        }
    }

}