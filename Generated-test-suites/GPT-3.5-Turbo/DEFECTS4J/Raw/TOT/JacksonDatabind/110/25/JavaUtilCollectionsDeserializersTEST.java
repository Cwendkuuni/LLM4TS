import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_withValidType_shouldReturnDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, listType);

        // Assert
        assertNotNull(deserializer);
        assertEquals(JavaUtilCollectionsDeserializers.StdDelegatingDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindForCollection_withInvalidType_shouldReturnNull() throws JsonMappingException {
        // Arrange
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JavaType setType = TypeFactory.defaultInstance().constructCollectionType(HashSet.class, Integer.class);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, setType);

        // Assert
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap_withValidType_shouldReturnDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, mapType);

        // Assert
        assertNotNull(deserializer);
        assertEquals(JavaUtilCollectionsDeserializers.StdDelegatingDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindForMap_withInvalidType_shouldReturnNull() throws JsonMappingException {
        // Arrange
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Double.class);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, listType);

        // Assert
        assertNull(deserializer);
    }
}