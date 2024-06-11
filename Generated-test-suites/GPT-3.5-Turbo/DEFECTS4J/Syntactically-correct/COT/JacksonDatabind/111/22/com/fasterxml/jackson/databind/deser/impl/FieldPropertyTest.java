import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null);
        JavaType type = new JavaType(null, null, null, null);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null);
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null, null, null);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        Assert.assertEquals(propDef, fieldProperty.getDefinition());
        Assert.assertEquals(type, fieldProperty.getType());
        Assert.assertEquals(typeDeser, fieldProperty.getValueTypeDeserializer());
        Assert.assertEquals(null, fieldProperty.getContextAnnotations());
        Assert.assertEquals(field, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        FieldProperty fieldProperty = getFieldProperty();

        Field field = fieldProperty.getClass().getDeclaredField("_field");
        field.setAccessible(true);

        Object instance = new Object();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        fieldProperty.deserializeAndSet(p, ctxt, instance);

        Assert.assertEquals(p, field.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        FieldProperty fieldProperty = getFieldProperty();

        Field field = fieldProperty.getClass().getDeclaredField("_field");
        field.setAccessible(true);

        Object instance = new Object();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);

        Assert.assertEquals(p, field.get(instance));
        Assert.assertEquals(instance, result);
    }

    @Test
    public void testSet() throws Exception {
        FieldProperty fieldProperty = getFieldProperty();

        Field field = fieldProperty.getClass().getDeclaredField("_field");
        field.setAccessible(true);

        Object instance = new Object();
        Object value = new Object();
        fieldProperty.set(instance, value);

        Assert.assertEquals(value, field.get(instance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        FieldProperty fieldProperty = getFieldProperty();

        Field field = fieldProperty.getClass().getDeclaredField("_field");
        field.setAccessible(true);

        Object instance = new Object();
        Object value = new Object();
        Object result = fieldProperty.setAndReturn(instance, value);

        Assert.assertEquals(value, field.get(instance));
        Assert.assertEquals(instance, result);
    }

    private FieldProperty getFieldProperty() throws NoSuchFieldException, IllegalAccessException {
        Field field = FieldProperty.class.getDeclaredField("_field");
        field.setAccessible(true);

        AnnotatedField annotatedField = new AnnotatedField(null, null, field);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null);
        JavaType type = new JavaType(null, null, null, null);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null);
        Annotations contextAnnotations = new Annotations();

        return new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
    }
}