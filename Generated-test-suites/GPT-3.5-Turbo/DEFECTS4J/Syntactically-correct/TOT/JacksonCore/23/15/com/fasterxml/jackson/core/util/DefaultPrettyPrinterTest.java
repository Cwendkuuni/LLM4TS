import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class DefaultPrettyPrinterTest {
    
    @Test
    public void testWriteRootValueSeparator() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(out);
        
        prettyPrinter.writeRootValueSeparator(generator);
        generator.close();
        
        assertEquals(" ", out.toString());
    }
    
    @Test
    public void testWriteStartObject() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(out);
        
        prettyPrinter.writeStartObject(generator);
        generator.close();
        
        assertEquals("{", out.toString());
    }
    
    @Test
    public void testBeforeObjectEntries() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(out);
        
        prettyPrinter.beforeObjectEntries(generator);
        generator.close();
        
        assertEquals("", out.toString());
    }
    
    @Test
    public void testWriteObjectFieldValueSeparator() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(out);
        
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        generator.close();
        
        assertEquals(": ", out.toString());
    }
    
    @Test
    public void testWriteObjectEntrySeparator() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(out);
        
        prettyPrinter.writeObjectEntrySeparator(generator);
        generator.close();
        
        assertEquals(", ", out.toString());
    }
    
    @Test
    public void testWriteEndObject() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(out);
                
        prettyPrinter.writeEndObject(generator, 0);
        generator.close();
        
        assertEquals(" }", out.toString());
    }
    
    @Test
    public void testWriteStartArray() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(out);
                
        prettyPrinter.writeStartArray(generator);
        generator.close();
        
        assertEquals("[", out.toString());
    }
    
    @Test
    public void testBeforeArrayValues() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(out);
                
        prettyPrinter.beforeArrayValues(generator);
        generator.close();
        
        assertEquals("", out.toString());
    }
    
    @Test
    public void testWriteArrayValueSeparator() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(out);
                
        prettyPrinter.writeArrayValueSeparator(generator);
        generator.close();
        
        assertEquals(", ", out.toString());
    }
    
    @Test
    public void testWriteEndArray() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(out);
                
        prettyPrinter.writeEndArray(generator, 0);
        generator.close();
        
        assertEquals(" ]", out.toString());
    }
    
    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter1 = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter2 = prettyPrinter1.withRootSeparator("SEPARATOR");
        
        assertNotEquals(prettyPrinter1, prettyPrinter2);
    }
    
    @Test
    public void testIndentArraysWith() {
        DefaultPrettyPrinter prettyPrinter1 = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter2 = prettyPrinter1.createInstance();
        
        prettyPrinter2.indentArraysWith(new DefaultPrettyPrinter.NopIndenter());
        assertNotEquals(prettyPrinter1, prettyPrinter2);
    }
    
    @Test
    public void testIndentObjectsWith() {
        DefaultPrettyPrinter prettyPrinter1 = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter2 = prettyPrinter1.createInstance();
        
        prettyPrinter2.indentObjectsWith(new DefaultPrettyPrinter.NopIndenter());
        assertNotEquals(prettyPrinter1, prettyPrinter2);
    }
    
    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter1 = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter2 = prettyPrinter1.createInstance();
        
        prettyPrinter2.withSpacesInObjectEntries();
        assertNotEquals(prettyPrinter1, prettyPrinter2);
    }
    
    @Test
    public void testWithoutSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter1 = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter2 = prettyPrinter1.createInstance();
        
        prettyPrinter2.withoutSpacesInObjectEntries();
        assertNotEquals(prettyPrinter1, prettyPrinter2);
    }
    
    @Test
    public void testWithSeparators() {
        DefaultPrettyPrinter prettyPrinter1 = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter2 = prettyPrinter1.createInstance();
        
        prettyPrinter2.withSeparators(new Separators());
        assertNotEquals(prettyPrinter1, prettyPrinter2);
    }  
}