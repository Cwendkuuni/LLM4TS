import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class XmlSerializerProviderTest {

    // Redirect standard output for testing
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream testOut = new ByteArrayOutputStream();

    @Before
    public void redirectStandardOutput() {
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void restoreStandardOutput() {
        System.setOut(originalOut);
    }

    @Test
    public void testSerializeValue_NullValue_ShouldCallSerializeXmlNull() throws IOException {
        // Arrange
        XmlMapper xmlMapper = new XmlMapper();
        XmlSerializerProvider provider = (XmlSerializerProvider) xmlMapper.getSerializerProvider();

        // Act
        provider.serializeValue(null, null);

        // Assert
        assertEquals("super.serializeValue() called with null value\n", testOut.toString());
    }

    @Test
    public void testSerializeValue_ValueAsArray_ShouldWriteStartRootArray() throws IOException {
        // Arrange
        XmlMapper xmlMapper = new XmlMapper();
        XmlSerializerProvider provider = (XmlSerializerProvider) xmlMapper.getSerializerProvider();
        TestClass[] value = new TestClass[2];
        value[0] = new TestClass("John", 30);
        value[1] = new TestClass("Jane", 25);
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        provider.serializeValue(xmlMapper.getFactory().createGenerator(outputStream), value);

        // Assert
        // XML output expectation:
        // <root>
        //     <item>
        //         <name>John</name>
        //         <age>30</age>
        //     </item>
        //     <item>
        //         <name>Jane</name>
        //         <age>25</age>
        //     </item>
        // </root>
        String xmlOutput = outputStream.toString();
        assertEquals("<root>", xmlOutput.substring(0, 6));
        assertEquals("<item>", xmlOutput.substring(7, 13));
        assertEquals("<name>John</name>", xmlOutput.substring(14, 29));
        assertEquals("<age>30</age>", xmlOutput.substring(30, 42));
        assertEquals("</item>", xmlOutput.substring(43, 50));
        assertEquals("<item>", xmlOutput.substring(51, 57));
        assertEquals("<name>Jane</name>", xmlOutput.substring(58, 73));
        assertEquals("<age>25</age>", xmlOutput.substring(74, 86));
        assertEquals("</item>", xmlOutput.substring(87, 94));
        assertEquals("</root>", xmlOutput.substring(95));
    }

    private static class TestClass {
        public String name;
        public int age;

        public TestClass(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}