
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testGetAnnotation() throws NoSuchMethodException {
        SetterlessProperty property = createSetterlessProperty();
        Class<Deprecated> annotationClass = Deprecated.class;
        Deprecated annotation = property.getAnnotation(annotationClass);
        assertNotNull(annotation);
    }

    @Test
    public void testGetMember() throws NoSuchMethodException {
        SetterlessProperty property = createSetterlessProperty();
        Method annotatedMethod = property.getMember().getAnnotated();
        Method expectedMethod = SetterlessProperty.class.getMethod("getGetter");
        assertEquals(expectedMethod, annotatedMethod);
    }

    @Test
    public void testDeserializeAndSetWithValueNull() throws Exception {
        SetterlessProperty property = createSetterlessProperty();
        TestObject instance = new TestObject();

        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("null");
        property.deserializeAndSet(jsonParser, new DeserializationContext(null), instance);

        assertNull(instance.getGetter());
    }

    @Test
    public void testDeserializeAndSetWithoutValueTypeDeserializer() throws Exception {
        SetterlessProperty property = createSetterlessProperty();
        TestObject instance = new TestObject();

        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("\"testValue\"");
        property.deserializeAndSet(jsonParser, new DeserializationContext(null), instance);

        assertEquals("testValue", instance.getGetter());
    }

    private SetterlessProperty createSetterlessProperty() throws NoSuchMethodException {
        Method method = SetterlessPropertyTest.class.getDeclaredMethod("getGetter");
        return new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, method, null, null));
    }

    private String getGetter() {
        return "getterValue";
    }
}

class TestObject {
    private String getter;

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }
}

