import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ser.PropertyFilter;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonFactory jsonFactory = new JsonFactory();

    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        anyGetterWriter = new AnyGetterWriter(null, null, null);
        TestJsonGenerator gen = new TestJsonGenerator();
        anyGetterWriter.getAndSerialize(null, gen, null);
        Assert.assertEquals("", gen.getOutput());
    }
    
    @Test
    public void testGetAndSerialize_NonMapValue() throws Exception {
        anyGetterWriter = new AnyGetterWriter(null, null, null);
        TestJsonGenerator gen = new TestJsonGenerator();
        try {
            anyGetterWriter.getAndSerialize(new Object(), gen, null);
            Assert.fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Value returned by 'any-getter' (null) not java.util.Map but java.lang.Object", e.getMessage());
        }
    }
    
    @Test
    public void testGetAndSerialize_WithMapSerializer() throws Exception {
        anyGetterWriter = new AnyGetterWriter(null, null, null);
        TestJsonGenerator gen = new TestJsonGenerator();
        anyGetterWriter._mapSerializer = new MapSerializer();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "value");
        anyGetterWriter.getAndSerialize(map, gen, null);
        Assert.assertEquals("{\"key\":\"value\"}", gen.getOutput());
    }
    
    @Test
    public void testGetAndFilter_NullValue() throws Exception {
        anyGetterWriter = new AnyGetterWriter(null, null, null);
        TestJsonGenerator gen = new TestJsonGenerator();
        anyGetterWriter.getAndFilter(null, gen, null, null);
        Assert.assertEquals("", gen.getOutput());
    }
    
    @Test
    public void testGetAndFilter_NonMapValue() throws Exception {
        anyGetterWriter = new AnyGetterWriter(null, null, null);
        TestJsonGenerator gen = new TestJsonGenerator();
        try {
            anyGetterWriter.getAndFilter(new Object(), gen, null, null);
            Assert.fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Value returned by 'any-getter' (null) not java.util.Map but java.lang.Object", e.getMessage());
        }
    }
    
    @Test
    public void testGetAndFilter_WithMapSerializer() throws Exception {
        anyGetterWriter = new AnyGetterWriter(null, null, null);
        TestJsonGenerator gen = new TestJsonGenerator();
        anyGetterWriter._mapSerializer = new MapSerializer();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "value");
        anyGetterWriter.getAndFilter(map, gen, null, null);
        Assert.assertEquals("{\"key\":\"value\"}", gen.getOutput());
    }
    
    @Test
    public void testResolve() throws Exception {
        anyGetterWriter = new AnyGetterWriter(null, null, null);
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public void filter(SerializableString name, Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                
            }
            
            @Override
            public void createContext(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
                
            }

            @Override
            public void setDefaultFilter(PropertyFilter defaultFilter) {

            }
        };
        anyGetterWriter.resolve(null);
        Assert.assertNotNull(anyGetterWriter._mapSerializer);
    }
}