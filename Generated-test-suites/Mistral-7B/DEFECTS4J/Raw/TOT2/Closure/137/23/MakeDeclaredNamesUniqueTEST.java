import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MakeDeclaredNamesUniqueTest {

 @Test
 public void testMakeDeclaredNamesUnique_GlobalScope() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 Node node = Node.createTree(
 "function f() {" +
 "  var a = 1;" +
 "  var b = 2;" +
 "  var c = a;" +
 "  function g() {" +
 "    var a = 3;" +
 "  }" +
 "}", 0);
 NodeTraversal traversal = new NodeTraversal(null, new MakeDeclaredNamesUnique());
 traversal.traverse(node);

 // Check that all variables have unique names in the global scope.
 Map<String, Integer> variableCountMap = new Maps.TreeMap<>();
 Iterator<Map.Entry<Scope.Var, String>> variableIterator = node.getScope().getVars().iterator();
 while (variableIterator.hasNext()) {
 String varName = variableIterator.next().getKey().getName();
 int count = variableCountMap.getOrDefault(varName, 0);
 variableCountMap.put(varName, count + 1);
 }
 assertEquals(3, variableCountMap.size());
 }

 @Test
 public void testMakeDeclaredNamesUnique_FunctionScope() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
 new InlineRenamer(
 new Supplier<String>() {
 @Override
 public String get() {
 return "0";
 }
 }, "_i", true));
 Node node = Node.createTree(
 "function f() {" +
 "  var a = 1;" +
 "  var b = 2;" +
 "  function g() {" +
 "    var a = 3;" +
 "  }" +
 "}", 0);
 NodeTraversal traversal = new NodeTraversal(null, new MakeDeclaredNamesUnique(makeDeclaredNamesUnique));
 traversal.traverse(node);

 // Check that all variables have unique names within the function scope.
 Map<String, Integer> variableCountMap = new Maps.TreeMap<>();
 Iterator<Map.Entry<Scope.Var, String>> variableIterator = node.getScope().getVars().iterator();
 while (variableIterator.hasNext()) {
 String varName = variableIterator.next().getKey().getName();
 int count = variableCountMap.getOrDefault(varName, 0);
 variableCountMap.put(varName, count + 1);
 }
 assertEquals(3, variableCountMap.size());
 // Check that the variable names are unique and have the suffix "_i"
 List<String> variableNames = Lists.newArrayList(variableIterator.asList().stream()
 .map(entry -> entry.getKey().getName()).collect(Collectors.toList()));
 Iterator<String> nameIterator = variableNames.iterator();
 while (nameIterator.hasNext()) {
 String name = nameIterator.next();
 int indexOfSeparator = indexOfSeparator(name);
 assertEquals(name.substring(0, indexOfSeparator), name.substring(indexOfSeparator + 1));
 }
 }

 @Test(expected = AssertionError.class)
 public void testMakeDeclaredNamesUnique_conflictingNames() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 Node node = Node.createTree(
 "function f() {" +
 "  var a = 1;" +
 "  var a = 2;" +
 "}", 0);
 NodeTraversal traversal = new NodeTraversal(null, new MakeDeclaredNamesUnique());
 traversal.traverse(node);
 }

 @Test(expected = AssertionError.class)
 public void testMakeDeclaredNamesUnique_invalidName() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 Node node = Node.createTree(
 "function f() {" +
 "  var 1 = 1;" +
 "}", 0);
 NodeTraversal traversal = new NodeTraversal(null, new MakeDeclaredNamesUnique());
 traversal.traverse(node);
 }

 @Test
 public void testMakeDeclaredNamesUnique_nestedFunctions() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 Node node = Node.createTree(
 "function f() {" +
 "  var a = 1;" +
 "  function g() {" +
 "    var a = 2;" +
 "  }" +
 "}", 0);
 NodeTraversal traversal = new NodeTraversal(null, new MakeDeclaredNamesUnique());
 traversal.traverse(node);

 // Check that all variables have unique names in the function g scope.
 Map<String, Integer> variableCountMap = new Maps.TreeMap<>();
 Iterator<Map.Entry<Scope.Var, String>> variableIterator = node.getScope(1).getVars().iterator();
 while (variableIterator.hasNext()) {
 String varName = variableIterator.next().getKey().getName();
 int count = variableCountMap.getOrDefault(varName, 0);
 variableCountMap.put(varName, count + 1);
 }
 assertEquals(2, variableCountMap.size());
 // Check that the variable names are unique
 List<String> variableNames = Lists.newArrayList(variableIterator.asList().stream()
 .map(entry -> entry.getKey().getName()).collect(Collectors.toList()));
 Iterator<String> nameIterator = variableNames.iterator();
 while (nameIterator.hasNext()) {
 String name = nameIterator.next();
 int indexOfSeparator = indexOfSeparator(name);
 String expectedName = (name.substring(0, indexOfSeparator).equals("a"))? "a1": name.substring(0, indexOfSeparator);
 assertEquals(expectedName, name.substring(0, indexOfSeparator));
 }
 }
}