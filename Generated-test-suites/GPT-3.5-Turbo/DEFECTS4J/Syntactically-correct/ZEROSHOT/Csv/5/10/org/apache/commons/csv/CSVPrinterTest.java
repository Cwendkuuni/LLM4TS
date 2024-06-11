import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {

    private StringWriter stringWriter;
    private CSVPrinter csvPrinter;
    private String expectedOutput;

    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
        stringWriter.close();
    }

    @Test
    public void testPrint_NullValue() throws IOException {
        csvPrinter.print(null);
        expectedOutput = "";
        Assert.assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrint_NonNullValue() throws IOException {
        csvPrinter.print("hello");
        expectedOutput = "hello";
        Assert.assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrint_Object_InputString() throws IOException {
        csvPrinter.print((Object) "hello");
        expectedOutput = "hello";
        Assert.assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrint_Object_InputInteger() throws IOException {
        csvPrinter.print((Object) 123);
        expectedOutput = "123";
        Assert.assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrint_Object_InputDouble() throws IOException {
        csvPrinter.print((Object) 12.34);
        expectedOutput = "12.34";
        Assert.assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrint_RecordSeparator() throws IOException {
        csvPrinter.println();
        expectedOutput = System.lineSeparator();
        Assert.assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrint_Record_Iterable() throws IOException {
        Iterable<Object> values = List.of("hello", "world");
        csvPrinter.printRecord(values);
        expectedOutput = "hello,world" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrint_Record_VarArgs() throws IOException {
        Object[] values = new Object[]{"hello", "world"};
        csvPrinter.printRecord(values);
        expectedOutput = "hello,world" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrint_Records_Iterable() throws IOException {
        Iterable<Object> values = List.of(
                new Object[]{"hello", "world"},
                new Object[]{"foo", "bar"}
        );
        csvPrinter.printRecords(values);
        expectedOutput = "hello,world" + System.lineSeparator() + "foo,bar" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrint_Records_VarArgs() throws IOException {
        Object[] values = new Object[]{
                new Object[]{"hello", "world"},
                new Object[]{"foo", "bar"}
        };
        csvPrinter.printRecords(values);
        expectedOutput = "hello,world" + System.lineSeparator() + "foo,bar" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, stringWriter.toString());
    }
}