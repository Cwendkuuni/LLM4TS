import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Test when root separator is null
        // Assert that no separator is written
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Test the start object is written correctly
        // Assert that the output is '{'
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Test the indentation before object entries is written correctly
        // Assert that the indentation matches the nesting level
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Test that the separator between object fields and values is written correctly
        // Assert that the separator matches the configuration of spacesInObjectEntries
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Test that the separator between object entries is written correctly
        // Assert that the separator matches the configuration of the separators
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Test the end object is written correctly
        // Assert that the output is a closing brace with the correct indentation
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Test the start array is written correctly
        // Assert that the output is '['
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Test the indentation before array values is written correctly
        // Assert that the indentation matches the nesting level
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Test that the separator between array values is written correctly
        // Assert that the separator matches the configuration of the separators
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Test the end array is written correctly
        // Assert that the output is a closing bracket with the correct indentation
    }
}