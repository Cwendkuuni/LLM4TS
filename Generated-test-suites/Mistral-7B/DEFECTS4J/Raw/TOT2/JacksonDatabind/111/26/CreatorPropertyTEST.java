// Merge test cases from all experts
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TypeUtil;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import java.io.IOException;

public class CreatorPropertyTest extends TestBase {

    private CreatorProperty property;

    @Test
    public void testCreatorPropertyBasicFunctionality() throws Exception {
        String value = "test value";
        property.set(null, value);
        assertEquals(value, property.get(null));
    }

    @Test
    public void testGetAnnotation() throws Exception {
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        when(param.getAnnotation(Annotation.class)).thenReturn(mock(Annotation.class));
        property = new CreatorProperty(new PropertyName("testProperty"), JavaType.of(String.class), null, null, null, param, 0, null, null);
        assertEquals(mock(Annotation.class), property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testGetFallbackSetter() throws IOException {
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property = new CreatorProperty(null, null);
        property.setFallbackSetter(fallbackSetter);
        assertSame(fallbackSetter, property._fallbackSetter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFallbackSetterNull() throws Exception {
        property = new CreatorProperty(null, null);
        property.setFallbackSetter(null);
    }

    @Test
    public void testIsIgnorable() {
        property = new CreatorProperty(null, null);
        assertFalse(property.isIgnorable());
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        Object beanInstance = new Object();
        Object injectedValue = "injected value";
        when(context.findInjectableValue(_eq(property._injectableValueId), _eq(property), _eq(beanInstance))).thenReturn(injectedValue);
        assertEquals(injectedValue, property.findInjectableValue(context, beanInstance));
    }

    @Test
    public void testInject() throws Exception {
        Object beanInstance = new Object();
        Object injectedValue = "injected value";
        property.inject(context, beanInstance);
        assertEquals(injectedValue, property.get(beanInstance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = "test value";
        property.set(instance, value);
        assertEquals(value, property.get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = "test value";
        assertEquals(value, property.setAndReturn(instance, value));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingSetter() throws Exception {
        JsonParser p = createParser("{\"testProperty\": \"test value\"}");
        property.deserializeAndSet(p, context, null);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String json = "{\"testProperty\": \"test value\"}";
        Object instance = new Object();
        property.deserializeAndSet(createParser(json), context, instance);
        assertEquals("test value", property.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        String json = "{\"testProperty\": \"test value\"}";
        Object instance = new Object();
        Object value = property.deserializeSetAndReturn(createParser(json), context, instance);
        assertEquals("test value", value);
    }

    @Test
    public void testMarkAsIgnorable() throws IOException {
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    // Add more test cases as needed
}