import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JavaUtilCollectionsDeserializersTest {

    private DeserializationContext ctxt;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        ctxt = mapper.getDeserializationContext();
    }

    @After
    public void tearDown() {
        ctxt = null;
    }

    @Test
    public void testFindForCollection_SingletonSet_SuccessfulDeserialization() throws JsonMappingException {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_SingletonSet_NullInput_ReturnsNull() throws JsonMappingException {
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, null);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet_SuccessfulDeserialization() throws JsonMappingException {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet_NullInput_ReturnsNull() throws JsonMappingException {
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, null);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testFindForCollection_AsList_SuccessfulDeserialization() throws JsonMappingException {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_AS_ARRAYS_LIST);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_AsList_NullInput_ReturnsNull() throws JsonMappingException {
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, null);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap_SingletonMap_SuccessfulDeserialization() throws JsonMappingException {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        // Assert
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_SingletonMap_NullInput_ReturnsNull() throws JsonMappingException {
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, null);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap_UnmodifiableMap_SuccessfulDeserialization() throws JsonMappingException {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        // Assert
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_UnmodifiableMap_NullInput_ReturnsNull() throws JsonMappingException {
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, null);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testConverter_SingletonSet_SuccessfulConversion() {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
        Object value = Collections.singleton(1);

        // Act
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type, Set.class);
        Object result = converter.convert(value);

        // Assert
        assertNotNull(result);
        assertEquals(result, Collections.singleton(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverter_SingletonSet_InvalidSize_ThrowsException() {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
        Object value = new HashSet<>();

        // Act
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type, Set.class);
        converter.convert(value);

        // Assert
        // Exception is thrown
    }

    @Test
    public void testConverter_SingletonList_SuccessfulConversion() {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
        Object value = Collections.singletonList(1);

        // Act
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, type, List.class);
        Object result = converter.convert(value);

        // Assert
        assertNotNull(result);
        assertEquals(result, Collections.singletonList(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverter_SingletonList_InvalidSize_ThrowsException() {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
        Object value = new ArrayList<>();

        // Act
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, type, List.class);
        converter.convert(value);

        // Assert
        // Exception is thrown
    }

    @Test
    public void testConverter_SingletonMap_SuccessfulConversion() {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        Object value = Collections.singletonMap("key", "value");

        // Act
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, type, Map.class);
        Object result = converter.convert(value);

        // Assert
        assertNotNull(result);
        assertEquals(result, Collections.singletonMap("key", "value"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverter_SingletonMap_InvalidSize_ThrowsException() {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        Object value = new HashMap<>();

        // Act
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, type, Map.class);
        converter.convert(value);

        // Assert
        // Exception is thrown
    }

    @Test
    public void testConverter_UnmodifiableSet_SuccessfulConversion() {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);
        Object value = Collections.unmodifiableSet(Collections.singleton(1));

        // Act
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, type, Set.class);
        Object result = converter.convert(value);

        // Assert
        assertNotNull(result);
        assertEquals(result, Collections.singleton(1));
    }

    @Test
    public void testConverter_UnmodifiableList_SuccessfulConversion() {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);
        Object value = Collections.unmodifiableList(Collections.singletonList(1));

        // Act
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, type, List.class);
        Object result = converter.convert(value);

        // Assert
        assertNotNull(result);
        assertEquals(result, Collections.singletonList(1));
    }

    @Test
    public void testConverter_UnmodifiableMap_SuccessfulConversion() {
        // Arrange
        JavaType type = ctxt.constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);
        Object value = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));

        // Act
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, type, Map.class);
        Object result = converter.convert(value);

        // Assert
        assertNotNull(result);
        assertEquals(result, Collections.singletonMap("key", "value"));
    }
}