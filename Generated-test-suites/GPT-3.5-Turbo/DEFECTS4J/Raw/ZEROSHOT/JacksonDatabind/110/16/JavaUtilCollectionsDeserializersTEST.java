import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
    
    private JavaUtilCollectionsDeserializers deserializers;

    @Before
    public void setUp() {
        deserializers = new JavaUtilCollectionsDeserializers() {};
    }

    @Test
    public void testFindForCollection() {
        try {
            assertNull(deserializers.findForCollection(null, null));
            assertNotNull(deserializers.findForCollection(null, JavaType.class));
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }

    @Test
    public void testFindForMap() {
        try {
            assertNull(deserializers.findForMap(null, null));
            assertNotNull(deserializers.findForMap(null, JavaType.class));
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }

    @Test
    public void testConverter() {
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = Object.class;
        JavaUtilCollectionsConverter converter = deserializers.converter(1, concreteType, rawSuper);
        assertNotNull(converter);
        assertEquals(concreteType, converter.getInputType(null));
        assertEquals(concreteType, converter.getOutputType(null));
    }

}