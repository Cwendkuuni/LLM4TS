import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for the CSVPrinter class.
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream outContent;
    private CSVPrinter printer;

    /**
     * Sets up the test environment.
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        outContent = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(outContent), CSVFormat.DEFAULT);
    }

    /**
     * Tears down the test environment.
     * @throws IOException
     */
    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    /**
     * Test printing normal values.
     * @throws IOException
     */
    @Test
    public void testPrintNormalValues() throws IOException {
        printer.print("Test");
        printer.println();
        printer.print(123);
        printer.println();
        printer.print(123.0);
        printer.println();
        printer.print(true);
        printer.println();
        printer.print(null);
        printer.println();
        printer.printRecords(Arrays.asList("Test1", 123, 123.0, true, null));
        printer.println();
        assertEquals("Test,Test\n123,123\n123.0,123.0\ntrue,true\nnull,null\nTest1,Test1\n123,123\n123.0,123.0\ntrue,true\nnull,null\n", outContent.toString());
    }

    /**
     * Test printing values with quoting.
     * @throws IOException
     */
    @Test
    public void testPrintQuotedValues() throws IOException {
        printer = new CSVPrinter(new OutputStreamWriter(outContent),
                CSVFormat.DEFAULT.withQuoteAll(true));
        printer.print("Test,Test \"Test\"");
        printer.println();
        printer.print(123);
        printer.println();
        printer.print(123.0);
        printer.println();
        printer.print(true);
        printer.println();
        printer.print(null);
        printer.println();
        printer.printRecords(Arrays.asList("Test,Test \"Test\"", 123, 123.0, true, null));
        printer.println();
        assertEquals("Test,\"Test, Test \"Test\"\"\n123,123\n123.0,123.0\ntrue,true\nnull,null\nTest,\"Test, Test \"Test\"\"\n123,123\n123.0,123.0\ntrue,true\nnull,null\n", outContent.toString());
    }

    /**
     * Test printing values with escaping.
     * @throws IOException
     */
    @Test
    public void testPrintEscapedValues() throws IOException {
        printer = new CSVPrinter(new OutputStreamWriter(outContent),
                CSVFormat.DEFAULT.withEscape('\\'));
        printer.print("Test,Test,Test\\Test,\"Test,Test,Test\\\\Test\",Test,\"Test,Test,Test\\\"Test\"");
        printer.println();
        printer.print(123);
        printer.println();
        printer.print(123.0);
        printer.println();
        printer.print(true);
        printer.println();
        printer.print(null);
        printer.println();
        printer.printRecords(Arrays.asList("Test,Test,Test\\Test,\"Test,Test,Test\\\\Test\",Test,\"Test,Test,Test\\\"Test\"",
                123, 123.0, true, null));
        printer.println();
        assertEquals("Test,Test,Test\\Test,\"Test,Test,Test\\\\Test\",Test,\"Test,Test,Test\\\"Test\"\n123,123\n123.0,123.0\ntrue,true\nnull,null\nTest,Test,Test\\Test,\"Test,Test,Test\\\\Test\",Test,\"Test,Test,Test\\\"Test\"\n123,123\n123.0,123.0\ntrue,true\nnull,null\n", outContent.toString());
    }

    /**
     * Test printing records with different quote policies.
     * @throws IOException
     */
    @Test
    public void testPrintRecordsWithQuotePolicies() throws IOException {
        printer = new CSVPrinter(new OutputStreamWriter(outContent), CSVFormat.DEFAULT);
        printer.printRecord(Arrays.asList("Test,Test \"Test\"", 123, 123.0, true, null));
        printer.println();
        printer.printRecord(Arrays.asList("Test,Test \"Test\"", 123, 123.0, true, null),
                CSVFormat.DEFAULT.withQuotePolicy(Quote.ALL));
        printer.println();
        printer.printRecord(Arrays.asList("Test,Test \"Test\"", 123, 123.0, true, null),
                CSVFormat.DEFAULT.withQuotePolicy(Quote.NON_NUMERIC));
        printer.println();
        printer.printRecord(Arrays.asList("Test,Test \"Test\"", 123, 123.0, true, null),
                CSVFormat.DEFAULT.withQuotePolicy(Quote.NONE));
        printer.println();
        printer.printRecord(Arrays.asList("Test,Test \"Test\"", 123, 123.0, true, null),
                CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL));
        printer.println();
        assertEquals("Test,Test \"Test\"\nTest,Test \"Test\"\nTest,Test \"Test\"\nTest,Test \"Test\"\nTest,Test \"Test\"\n", outContent.toString());
    }

    /**
     * Test printing comments.
     * @throws IOException
     */
    @Test
    public void testPrintComments() throws IOException {
        final List<String> expected = new ArrayList<>();
        expected.add("Test1,Test1,Test1");
        expected.add("# This is a comment");
        expected.add("Test2,Test2,Test2");
        printer = new CSVPrinter(new OutputStreamWriter(outContent), CSVFormat.DEFAULT.withComment("Test "));
        printer.printRecords(Arrays.asList("Test1,Test1,Test1"), Arrays.asList("Test2,Test2,Test2"), Arrays.asList("# This is a comment"));
        printer.println();
        printer.printComment("Another comment");
        printer.println();
        assertEquals("Test1,Test1,Test1\n# This is a comment\nTest2,Test2,Test2\nAnother comment\n", outContent.toString());
    }
}