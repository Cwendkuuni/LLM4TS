import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;

public class JavaUtilCollectionsDeserializersTest {

    // Test for findForCollection method
    
    @Test
    public void testFindForCollection() throws JsonMappingException {

        DeserializationContext ctxt = null;
        JavaType type = null;

        // Test for CLASS_AS_ARRAYS_LIST
        type = JavaType.construct(CLASS_AS_ARRAYS_LIST);
        assertNotNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, type));

        // Test for CLASS_SINGLETON_LIST
        type = JavaType.construct(CLASS_SINGLETON_LIST);
        assertNotNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, type));

        // Test for CLASS_SINGLETON_SET
        type = JavaType.construct(CLASS_SINGLETON_SET);
        assertNotNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, type));

        // Test for CLASS_UNMODIFIABLE_LIST
        type = JavaType.construct(CLASS_UNMODIFIABLE_LIST);
        assertNotNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, type));

        // Test for CLASS_UNMODIFIABLE_SET
        type = JavaType.construct(CLASS_UNMODIFIABLE_SET);
        assertNotNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, type));

        // Test for other cases
        type = JavaType.construct(String.class);
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, type));
    }

    // Test for findForMap method
    
    @Test
    public void testFindForMap() throws JsonMappingException {

        DeserializationContext ctxt = null;
        JavaType type = null;

        // Test for CLASS_SINGLETON_MAP
        type = JavaType.construct(CLASS_SINGLETON_MAP);
        assertNotNull(JavaUtilCollectionsDeserializers.findForMap(ctxt, type));

        // Test for CLASS_UNMODIFIABLE_MAP
        type = JavaType.construct(CLASS_UNMODIFIABLE_MAP);
        assertNotNull(JavaUtilCollectionsDeserializers.findForMap(ctxt, type));

        
        // Test for other cases
        type = JavaType.construct(String.class);
        assertNull(JavaUtilCollectionsDeserializers.findForMap(ctxt, type));
    }

    // Test for converter method
    
    @Test
    public void testConverter() {

        int kind = 1;
        JavaType concreteType = JavaType.construct(String.class);
        Class<?> rawSuper = List.class;

        assertNotNull(JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper));
    }

    // Test for convert method
    
    @Test
    public void testConvert() {

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(kind, concreteType);

        assertNull(converter.convert(null));
        
        Object value = Collections.singleton("test string");
        assertEquals(Collections.singleton(value.iterator().next()), converter.convert(value));

        value = new ArrayList<>();
        value.add("test string");
        assertEquals(Collections.singletonList(value.get(0)), converter.convert(value));

        value = new HashMap<>();
        value.put("key", "test string");
        Map.Entry<?,?> entry = value.entrySet().iterator().next();
        assertEquals(Collections.singletonMap(entry.getKey(), entry.getValue()), converter.convert(value));

        value = new HashSet<>();
        value.add("test string");
        assertEquals(Collections.unmodifiableSet(value), converter.convert(value));

        value = new ArrayList<>();
        value.add("test string");
        assertEquals(Collections.unmodifiableList(value), converter.convert(value));

        value = new HashMap<>();
        value.put("key", "test string");
        assertEquals(Collections.unmodifiableMap(value), converter.convert(value));

        value = new ArrayList<>();
        value.add("test string");
        assertEquals(value, converter.convert(value));
    }

    // Test for getInputType method
    
    @Test
    public void testGetInputType() {

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(kind, concreteType);

        assertEquals(concreteType, converter.getInputType(typeFactory));
    }

    // Test for getOutputType method
    
    @Test
    public void testGetOutputType() {

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(kind, concreteType);

        assertEquals(concreteType, converter.getOutputType(typeFactory));
    }

    // Test for _checkSingleton method
    
    @Test(expected = IllegalArgumentException.class)
    public void test_checkSingleton() {

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(kind, concreteType);

        int size = 0;
        converter._checkSingleton(size);

        size = 2;
        converter._checkSingleton(size);
    }
}