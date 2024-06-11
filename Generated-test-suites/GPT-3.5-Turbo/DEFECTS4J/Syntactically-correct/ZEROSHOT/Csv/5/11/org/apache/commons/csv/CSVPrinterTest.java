import org.apache.commons.csv.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringBuilder stringBuilder;
    private CSVFormat csvFormat;

    @Before
    public void setUp() {
        stringBuilder = new StringBuilder();
        csvFormat = CSVFormat.DEFAULT;
        csvPrinter = new CSVPrinter(stringBuilder, csvFormat);
    }

    @After
    public void tearDown() {
        csvPrinter = null;
        stringBuilder = null;
        csvFormat = null;
    }

    @Test
    public void testPrint() throws Exception {
        csvPrinter.print("Hello");
        csvPrinter.print("World");
        assertEquals("Hello,World", stringBuilder.toString());
    }

    @Test
    public void testPrint_NullValue() throws Exception {
        csvPrinter.print(null);
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testPrintComment() throws Exception {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecord() throws Exception {
        csvPrinter.printRecord("Apple", "Banana", "Cherry");
        assertEquals("Apple,Banana,Cherry\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecord_NullValue() throws Exception {
        csvPrinter.printRecord("Apple", null, "Cherry");
        assertEquals("Apple,,Cherry\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecords() throws Exception {
        Iterable<String> record1 = new ArrayList<String>(Arrays.asList("Apple", "Banana", "Cherry"));
        Iterable<String> record2 = new ArrayList<String>(Arrays.asList("Dog", "Elephant", "Fox"));
        csvPrinter.printRecords(Arrays.asList(record1, record2));
        assertEquals("Apple,Banana,Cherry\nDog,Elephant,Fox\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecords_EmptyCollection() throws Exception {
        csvPrinter.printRecords(Collections.emptyList());
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testGetOut() throws Exception {
        assertEquals(stringBuilder, csvPrinter.getOut());
    }
}