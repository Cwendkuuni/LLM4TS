
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
    
    private AnyGetterWriter anyGetterWriter;
    
    @Before
    public void setUp() {
        BeanProperty property = new BeanProperty();
        AnnotatedMember accessor = new AnnotatedMember();
        MapSerializer serializer = new MapSerializer();
        anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
    }
    
    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        try {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
            fail("Expected exception JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (_accessor.getName()) not java.util.Map but " + bean.getClass().getName(), e.getMessage());
        }
    }
    
    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        
        try {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
            fail("Expected exception JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (_accessor.getName()) not java.util.Map but " + bean.getClass().getName(), e.getMessage());
        }
    }
    
    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        
        try {
            anyGetterWriter.resolve(provider);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException occurred");
        }
        
        assertNotNull(anyGetterWriter._mapSerializer);
    }
    
}
