
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReflectiveTypeAdapterFactoryTest {
    
    private ReflectiveTypeAdapterFactory.BoundField field;
    
    @Before
    public void setup() {
        field = new ReflectiveTypeAdapterFactory.BoundField("testField", true, true) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return true;
            }

            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                writer.value("test");
            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
                // do nothing
            }
        };
    }
    
    @Test
    public void testWriteField() throws IOException, IllegalAccessException {
        boolean result = field.writeField(new Object());
        assertEquals(true, result);
    }
    
    @Test
    public void testWrite() throws IOException, IllegalAccessException {
        JsonWriter writer = new JsonWriter(new StringWriter());
        field.write(writer, new Object());
        writer.flush();
        assertEquals("test", writer.toString());
    }
    
    @Test
    public void testRead() throws IOException, IllegalAccessException {
        JsonReader reader = new JsonReader(new StringReader("test"));
        field.read(reader, new Object());
    }
    
    @Test
    public void testReadWithNullValue() throws IOException, IllegalAccessException {
        JsonReader reader = new JsonReader(new StringReader("null"));
        field.read(reader, new Object());
    }
}
