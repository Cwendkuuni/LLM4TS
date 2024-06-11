import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstructor() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertNotNull(methodProperty);
    }

    @Test
    public void testMethodPropertyCopyConstructorWithDeserializer() {
        MethodProperty src = null;
        JsonDeserializer<?> deser = null;
        NullValueProvider nva = null;

        MethodProperty methodProperty = new MethodProperty(src, deser, nva);

        assertNotNull(methodProperty);
    }

    @Test
    public void testMethodPropertyCopyConstructorWithNewName() {
        MethodProperty src = null;
        PropertyName newName = null;

        MethodProperty methodProperty = new MethodProperty(src, newName);

        assertNotNull(methodProperty);
    }

    @Test
    public void testMethodPropertyCopyConstructorWithMethod() {
        MethodProperty src = null;
        Method m = null;

        MethodProperty methodProperty = new MethodProperty(src, m);

        assertNotNull(methodProperty);
    }

    @Test
    public void testMethodPropertyWithName() {
        PropertyName newName = null;

        MethodProperty methodProperty = new MethodProperty();

        SettableBeanProperty result = methodProperty.withName(newName);

        assertNotNull(result);
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() {
        JsonDeserializer<?> deser = null;

        MethodProperty methodProperty = new MethodProperty();

        SettableBeanProperty result = methodProperty.withValueDeserializer(deser);

        assertNotNull(result);
    }

    @Test
    public void testMethodPropertyWithNullProvider() {
        NullValueProvider nva = null;

        MethodProperty methodProperty = new MethodProperty();

        SettableBeanProperty result = methodProperty.withNullProvider(nva);

        assertNotNull(result);
    }

    @Test
    public void testMethodPropertyFixAccess() {
        DeserializationConfig config = null;

        MethodProperty methodProperty = new MethodProperty();

        methodProperty.fixAccess(config);
    }

    @Test
    public void testMethodPropertyGetAnnotation() {
        Class<A> acls = null;

        MethodProperty methodProperty = new MethodProperty();

        A result = methodProperty.getAnnotation(acls);

        assertNull(result);
    }

    @Test
    public void testMethodPropertyGetMember() {
        MethodProperty methodProperty = new MethodProperty();

        AnnotatedMember result = methodProperty.getMember();

        assertNotNull(result);
    }

    @Test
    public void testMethodPropertyDeserializeAndSet() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        MethodProperty methodProperty = new MethodProperty();

        methodProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testMethodPropertyDeserializeSetAndReturn() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        MethodProperty methodProperty = new MethodProperty();

        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertNotNull(result);
    }

    @Test
    public void testMethodPropertySet() {
        Object instance = null;
        Object value = null;

        MethodProperty methodProperty = new MethodProperty();

        methodProperty.set(instance, value);
    }

    @Test
    public void testMethodPropertySetAndReturn() {
        Object instance = null;
        Object value = null;

        MethodProperty methodProperty = new MethodProperty();

        Object result = methodProperty.setAndReturn(instance, value);

        assertNotNull(result);
    }

    @Test
    public void testMethodPropertyReadResolve() {
        MethodProperty methodProperty = new MethodProperty();

        Object result = methodProperty.readResolve();

        assertNotNull(result);
    }
}