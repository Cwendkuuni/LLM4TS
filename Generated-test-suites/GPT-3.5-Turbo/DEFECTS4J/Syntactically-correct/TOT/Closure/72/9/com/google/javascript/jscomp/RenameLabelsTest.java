import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

        // Define 3 different test cases for the process method
        // Test case 1: Typical use case
        // Test case 2: Edge case - when there are no labels in the code
        // Test case 3: Error scenario - when the compiler is null

        // Test case 1: Typical use case - test renaming labels in the code
        // Create a sample code with labels
        String code1 = "function foo() { \n" +
                "a: { \n" +
                "b: { \n" +
                "console.log('Inside label b'); \n" +
                "} \n" +
                "console.log('Inside label a'); \n" +
                "} \n" +
                "}";

        // Set the input code for the compiler
        compiler.setInputCode(code1);
        // Call the process() method
        renameLabels.process(compiler, compiler.getRoot());

        // Check if the labels are renamed correctly
        String expectedCode1 = "function foo() { \n" +
                "a1: { \n" +
                "b1: { \n" +
                "console.log('Inside label b'); \n" +
                "} \n" +
                "console.log('Inside label a'); \n" +
                "} \n" +
                "}";
        assertEquals(expectedCode1, compiler.getOutputCode());


        // Test case 2: Edge case - when there are no labels in the code
        // Create a sample code without any labels
        String code2 = "function foo() { console.log('No labels'); }";

        // Set the input code for the compiler
        compiler.setInputCode(code2);
        // Call the process() method
        renameLabels.process(compiler, compiler.getRoot());

        // Check if the code remains unchanged
        assertEquals(code2, compiler.getOutputCode());


        // Test case 3: Error scenario - when the compiler is null
        try {
            renameLabels.process(null, null);
            // If an exception is not thrown, the test should fail
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception, the test passes
        }
    }

    @Test
    public void testGetNamesForId() {
        RenameLabels renameLabels = new RenameLabels(new AbstractCompiler(), new DefaultNameSupplier(), true);

        // Define 3 different test cases for the getNameForId method
        // Test case 1: Typical use case
        // Test case 2: Edge case - when the id is 0
        // Test case 3: Edge case - when the id is negative

        // Test case 1: Typical use case - get names for valid ids
        // Add sample names to the renameLabels object
        renameLabels.names.add("a");
        renameLabels.names.add("b");
        renameLabels.names.add("c");

        // Check the names for different ids
        assertEquals("a", renameLabels.getNameForId(1));
        assertEquals("b", renameLabels.getNameForId(2));
        assertEquals("c", renameLabels.getNameForId(3));

        // Test case 2: Edge case - when the id is 0
        // Check if invoking with id 0 throws an exception
        try {
            renameLabels.getNameForId(0);
            // If an exception is not thrown, the test should fail
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception, the test passes
        }

        // Test case 3: Edge case - when the id is negative
        // Check if invoking with a negative id throws an exception
        try {
            renameLabels.getNameForId(-1);
            // If an exception is not thrown, the test should fail
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception, the test passes
        }
    }

    @Test
    public void testGetLabelInfo() {
        RenameLabels renameLabels = new RenameLabels(new AbstractCompiler(), new DefaultNameSupplier(), true);

        // Define 3 different test cases for the getLabelInfo() method
        // Test case 1: Typical use case
        // Test case 2: Edge case - when the label name does not exist in the current context
        // Test case 3: Error scenario - when the current namespace stack is empty

        // Test case 1: Typical use case - get label info for existing label names
        // Add sample label info to the renameLabels object
        RenameLabels.LabelNamespace namespace = renameLabels.new LabelNamespace();
        RenameLabels.LabelInfo info1 = renameLabels.new LabelInfo(1);
        RenameLabels.LabelInfo info2 = renameLabels.new LabelInfo(2);
        RenameLabels.LabelInfo info3 = renameLabels.new LabelInfo(3);
        namespace.renameMap.put("a", info1);
        namespace.renameMap.put("b", info2);
        namespace.renameMap.put("c", info3);
        renameLabels.namespaceStack.push(namespace);

        // Check the label info for different label names
        assertEquals(info1, renameLabels.getLabelInfo("a"));
        assertEquals(info2, renameLabels.getLabelInfo("b"));
        assertEquals(info3, renameLabels.getLabelInfo("c"));

        // Test case 2: Edge case - when the label name does not exist in the current context
        // Check if invoking with a non-existent label name returns null
        assertNull(renameLabels.getLabelInfo("d"));

        // Test case 3: Error scenario - when the current namespace stack is empty
        // Remove the namespace from the namespace stack
        renameLabels.namespaceStack.pop();

        // Check if invoking getLabelInfo() throws an exception when the namespace stack is empty
        try {
            renameLabels.getLabelInfo("a");
            // If an exception is not thrown, the test should fail
            fail("Expected EmptyStackException");
        } catch (EmptyStackException e) {
            // Expected exception, the test passes
        }
    }
}