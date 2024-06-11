
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollection_WhenTypeIsArraysList_ReturnNotNullDeserialize() throws Exception {
        // Arrange
        JavaType type = mapper.getTypeFactory().constructType(Arrays.asList().getClass());

        // Act
        Converter<?, ?> result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindForCollection_WhenTypeIsSingletonList_ReturnNotNullDeserialize() throws Exception {
        // Arrange
        JavaType type = mapper.getTypeFactory().constructType(Collections.singletonList().getClass());

        // Act
        Converter<?, ?> result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindForCollection_WhenTypeIsSingletonSet_ReturnNotNullDeserialize() throws Exception {
        // Arrange
        JavaType type = mapper.getTypeFactory().constructType(Collections.singletonSet().getClass());

        // Act
        Converter<?, ?> result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindForCollection_WhenTypeIsUnmodifiableList_ReturnNotNullDeserialize() throws Exception {
        // Arrange
        JavaType type = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Collections.singletonList()).getClass());

        // Act
        Converter<?, ?> result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindForCollection_WhenTypeIsUnmodifiableSet_ReturnNotNullDeserialize() throws Exception {
        // Arrange
        JavaType type = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Collections.singletonSet()).getClass());

        // Act
        Converter<?, ?> result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindForCollection_WhenTypeIsNotSpecialContainer_ReturnNull() throws Exception {
        // Arrange
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);

        // Act
        Converter<?, ?> result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

        // Assert
        assertNull(result);
    }

    @Test
    public void testFindForMap_WhenTypeIsSingletonMap_ReturnNotNullDeserialize() throws Exception {
        // Arrange
        JavaType type = mapper.getTypeFactory().constructType(Collections.singletonMap().getClass());

        // Act
        Converter<?, ?> result = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindForMap_WhenTypeIsUnmodifiableMap_ReturnNotNullDeserialize() throws Exception {
        // Arrange
        JavaType type = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(Collections.singletonMap()).getClass());

        // Act
        Converter<?, ?> result = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindForMap_WhenTypeIsNotSpecialContainer_ReturnNull() throws Exception {
        // Arrange
        JavaType type = mapper.getTypeFactory().constructType(HashMap.class);

        // Act
        Converter<?, ?> result = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);

        // Assert
        assertNull(result);
    }
}
