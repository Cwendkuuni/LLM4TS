import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = typeFromClass(JavaUtilCollectionsDeserializers.CLASS_AS_ARRAYS_LIST);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        verify(ctxt, times(1)).findConverter(eq(type));
    }
}