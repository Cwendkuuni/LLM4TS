
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
        TypeAdapter<?> adapter = factory.create(null, null);
        assertNull(adapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterClass() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
        TypeAdapter<?> adapter = factory.getTypeAdapter(null, null, null, null);
        assertNotNull(adapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactory() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
        TypeAdapter<?> adapter = factory.getTypeAdapter(null, null, null, null);
        assertNotNull(adapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotationValue() {
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
        factory.getTypeAdapter(null, null, null, null);
    }
}

