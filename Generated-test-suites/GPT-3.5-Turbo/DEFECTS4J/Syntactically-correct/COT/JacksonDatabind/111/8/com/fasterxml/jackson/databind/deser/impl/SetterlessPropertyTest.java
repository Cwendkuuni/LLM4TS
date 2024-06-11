import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Assert;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetterlessPropertyTest {

    @Test
    public void testConstructorAndGetters() throws NoSuchMethodException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        Assert.assertEquals(annotated, property.getMember());
        Assert.assertEquals(getter, property.getAnnotatedGetter());
    }

    @Test
    public void testWithName() throws NoSuchMethodException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty newProperty = property.withName(newName);

        Assert.assertNotEquals(property, newProperty);
        Assert.assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() throws NoSuchMethodException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        SettableBeanProperty newProperty = property.withValueDeserializer(deserializer);

        Assert.assertNotEquals(property, newProperty);
        Assert.assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() throws NoSuchMethodException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        NullValueProvider provider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        SettableBeanProperty newProperty = property.withNullProvider(provider);

        Assert.assertNotEquals(property, newProperty);
        Assert.assertEquals(provider, newProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() throws NoSuchMethodException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        DeserializationConfig config = new DeserializationConfig(null, new ObjectMapper().getMapperFactory(),
                new SimpleTypeResolver(), new SimpleInjector(), null, null);
        property.fixAccess(config);
        // Assert no exception is thrown
    }

    @Test
    public void testGetAnnotation() throws NoSuchMethodException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        Annotation annotation = property.getAnnotation(Test.class);

        Assert.assertNull(annotation);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws NoSuchMethodException, IOException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        property.set(new Object(), new Object());
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws NoSuchMethodException, IOException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        property.deserializeAndSet(
                new JsonParser() {
                    @Override
                    public JsonToken nextToken() throws IOException {
                        return null;
                    }

                    @Override
                    public JsonToken getCurrentToken() {
                        return JsonToken.VALUE_NULL;
                    }

                    @Override
                    public void close() throws IOException {
                    }
                },
                null, new Object()
        );
        // Assert no exception is thrown
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWhenGetterThrowsException() throws NoSuchMethodException, IOException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        property.deserializeAndSet(
                new JsonParser() {
                    @Override
                    public JsonToken nextToken() throws IOException {
                        return null;
                    }

                    @Override
                    public JsonToken getCurrentToken() {
                        return JsonToken.VALUE_FALSE;
                    }

                    @Override
                    public void close() throws IOException {
                    }
                },
                null, new Object()
        );
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValueAndNullGetterValue() throws NoSuchMethodException, IOException {
        Method nullGetter = SetterlessProperty.class.getDeclaredMethod("nullGetter");
        AnnotatedMethod annotated = new AnnotatedMethod(nullGetter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        property.deserializeAndSet(
                new JsonParser() {
                    @Override
                    public JsonToken nextToken() throws IOException {
                        return null;
                    }

                    @Override
                    public JsonToken getCurrentToken() {
                        return JsonToken.VALUE_NULL;
                    }

                    @Override
                    public void close() throws IOException {
                    }
                },
                null, new Object()
        );
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNonNullValueAndNullGetterValue() throws NoSuchMethodException, IOException {
        Method nullGetter = SetterlessProperty.class.getDeclaredMethod("nullGetter");
        AnnotatedMethod annotated = new AnnotatedMethod(nullGetter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        property.deserializeAndSet(
                new JsonParser() {
                    @Override
                    public JsonToken nextToken() throws IOException {
                        return null;
                    }

                    @Override
                    public JsonToken getCurrentToken() {
                        return JsonToken.VALUE_NUMBER_INT;
                    }

                    @Override
                    public BigDecimal getDecimalValue() throws IOException {
                        return BigDecimal.ZERO;
                    }

                    @Override
                    public void close() throws IOException {
                    }
                },
                null, new Object()
        );
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNonNullValueAndNullSetterValue() throws NoSuchMethodException, IOException {
        Method nullSetter = SetterlessProperty.class.getDeclaredMethod("nullSetter", Object.class);
        AnnotatedMethod annotated = new AnnotatedMethod(nullSetter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        property.deserializeAndSet(
                new JsonParser() {
                    @Override
                    public JsonToken nextToken() throws IOException {
                        return null;
                    }

                    @Override
                    public JsonToken getCurrentToken() {
                        return JsonToken.VALUE_NUMBER_INT;
                    }

                    @Override
                    public BigDecimal getDecimalValue() throws IOException {
                        return BigDecimal.ZERO;
                    }

                    @Override
                    public void close() throws IOException {
                    }
                },
                null, new Object()
        );
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNonNullValueAndInvalidReturnType() throws NoSuchMethodException, IOException {
        Method invalidReturnType = SetterlessProperty.class.getDeclaredMethod("invalidReturnType");
        AnnotatedMethod annotated = new AnnotatedMethod(invalidReturnType, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        property.deserializeAndSet(
                new JsonParser() {
                    @Override
                    public JsonToken nextToken() throws IOException {
                        return null;
                    }

                    @Override
                    public JsonToken getCurrentToken() {
                        return JsonToken.VALUE_NUMBER_INT;
                    }

                    @Override
                    public BigDecimal getDecimalValue() throws IOException {
                        return BigDecimal.ZERO;
                    }

                    @Override
                    public void close() throws IOException {
                    }
                },
                null, new Object()
        );
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNonNullValueAndSetterThrowsException() throws NoSuchMethodException, IOException {
        Method setterWithException = SetterlessProperty.class.getDeclaredMethod("setterWithException", Object.class);
        AnnotatedMethod annotated = new AnnotatedMethod(setterWithException, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        property.deserializeAndSet(
                new JsonParser() {
                    @Override
                    public JsonToken nextToken() throws IOException {
                        return null;
                    }

                    @Override
                    public JsonToken getCurrentToken() {
                        return JsonToken.VALUE_NUMBER_INT;
                    }

                    @Override
                    public BigDecimal getDecimalValue() throws IOException {
                        return BigDecimal.ZERO;
                    }

                    @Override
                    public void close() throws IOException {
                    }
                },
                null, new Object()
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws NoSuchMethodException, IOException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        property.setAndReturn(new Object(), new Object());
    }

    @Test
    public void testDeserializeSetAndReturn() throws NoSuchMethodException, IOException {
        Method getter = SetterlessProperty.class.getDeclaredMethod("_getter");
        AnnotatedMethod annotated = new AnnotatedMethod(getter, null, null);
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(null, null, null),
                null, null, null, annotated
        );

        Object instance = new Object();
        Object value = new Object();
        Object result = property.deserializeSetAndReturn(null, null, instance);

        Assert.assertEquals(instance, result);
        // Assert no exception is thrown
    }

    // Helper methods for testing
    public Object nullGetter() {
        return null;
    }

    public void nullSetter(Object value) {
        // do nothing
    }

    public int invalidReturnType() {
        return 0;
    }

    public void setterWithException(Object value) throws IOException {
        throw new IOException();
    }

}