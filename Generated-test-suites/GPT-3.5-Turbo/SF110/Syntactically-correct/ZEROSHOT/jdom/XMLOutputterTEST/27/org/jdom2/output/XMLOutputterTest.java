import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import java.nio.charset.StandardCharsets;

public class XMLOutputterTest {

    // Test output() method for Document
    @Test
    public void testOutputDocument() {
        try {
            Format format = Format.getPrettyFormat();
            XMLOutputter outputter = new XMLOutputter(format);
        
            Document doc = new Document();
            Element root = new Element("root");
            doc.setRootElement(root);
        
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputter.output(doc, outputStream);
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root/>\n";
            assertEquals(expected, outputStream.toString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            fail("Output failed: " + e.getMessage());
        }
    }

    // Test output() method for DocType
    @Test
    public void testOutputDocType() {
        try {
            Format format = Format.getPrettyFormat();
            XMLOutputter outputter = new XMLOutputter(format);
        
            DocType doctype = new DocType("root");
        
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputter.output(doctype, outputStream);
            String expected = "<!DOCTYPE root>\n";
            assertEquals(expected, outputStream.toString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            fail("Output failed: " + e.getMessage());
        }
    }

    // Test output() method for Element
    @Test
    public void testOutputElement() {
        try {
            Format format = Format.getPrettyFormat();
            XMLOutputter outputter = new XMLOutputter(format);
        
            Element element = new Element("root");
        
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputter.output(element, outputStream);
            String expected = "<root/>\n";
            assertEquals(expected, outputStream.toString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            fail("Output failed: " + e.getMessage());
        }
    }

    // Test output() method for List of Content
    @Test
    public void testOutputList() {
        try {
            Format format = Format.getPrettyFormat();
            XMLOutputter outputter = new XMLOutputter(format);
        
            Element root = new Element("root");
            Content c1 = new Element("child1");
            Content c2 = new Element("child2");
            List<Content> list = new ArrayList<>();
            list.add(c1);
            list.add(c2);
            root.setContent(list);
        
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputter.output(list, outputStream);
            String expected = "<child1/>\n<child2/>\n";
            assertEquals(expected, outputStream.toString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            fail("Output failed: " + e.getMessage());
        }
    }
    
    // Add more test cases to cover other methods if required

    @BeforeClass
    public static void setUpClass() {
        System.out.println("");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("");
    }

    @Before
    public void setUp() {
        // Set up code
    }

    @After
    public void tearDown() {
        // Tear down code
    }
}