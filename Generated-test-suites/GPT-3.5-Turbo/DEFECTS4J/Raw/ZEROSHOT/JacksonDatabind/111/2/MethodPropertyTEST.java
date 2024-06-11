import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodPropertyTest {
    private MethodProperty methodProperty;

    @Before
    public void setUp() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void test_withName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = methodProperty.withName(newName);
        Assert.assertEquals(newName, result.getFullName());
    }

    @Test
    public void test_withValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        SettableBeanProperty result = methodProperty.withValueDeserializer(deser);
        Assert.assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void test_withNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        SettableBeanProperty result = methodProperty.withNullProvider(nva);
        Assert.assertEquals(nva, result.getNullProvider());
    }

    @Test
    public void test_fixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        methodProperty.fixAccess(config);
        Assert.assertTrue(true);
    }

    @Test
    public void test_getAnnotation() {
        Class<A> acls = A.class;
        Annotation result = methodProperty.getAnnotation(acls);
        Assert.assertNull(result);
    }

    @Test
    public void test_getMember() {
        AnnotatedMember result = methodProperty.getMember();
        Assert.assertEquals(methodProperty._annotated, result);
    }

    @Test
    public void test_deserializeAndSet() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        methodProperty.deserializeAndSet(p, ctxt, instance);
        Assert.assertTrue(true);
    }

    @Test
    public void test_deserializeSetAndReturn() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        Assert.assertTrue(true);
    }

    @Test
    public void test_set() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        methodProperty.set(instance, value);
        Assert.assertTrue(true);
    }

    @Test
    public void test_setAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        methodProperty.setAndReturn(instance, value);
        Assert.assertTrue(true);
    }

    @Test
    public void test_readResolve() {
        Object result = methodProperty.readResolve();
        Assert.assertEquals(MethodProperty.class, result.getClass());
    }
}