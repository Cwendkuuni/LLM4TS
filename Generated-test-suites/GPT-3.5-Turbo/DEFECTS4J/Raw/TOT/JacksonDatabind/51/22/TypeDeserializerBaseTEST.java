import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deser = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public TypeIdResolver getTypeIdResolver() {
                return null;
            }

            @Override
            public Class<?> getDefaultImpl() {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public String getPropertyName() {
                return null;
            }
        };

        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", deser.baseTypeName());
    }

    @Test
    public void testGetPropertyname() {
        TypeDeserializerBase deser = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public TypeIdResolver getTypeIdResolver() {
                return null;
            }

            @Override
            public Class<?> getDefaultImpl() {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public String getPropertyName() {
                return "type";
            }
        };

        assertEquals("type", deser.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase deser = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public TypeIdResolver getTypeIdResolver() {
                return new TypeIdResolver() {
                    @Override
                    public void init(JavaType baseType) {

                    }

                    @Override
                    public String idFromValue(Object value) {
                        return null;
                    }

                    @Override
                    public String idFromValueAndType(Object value, Class<?> suggestedType) {
                        return null;
                    }

                    @Override
                    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
                        if (id.equals("1")) {
                            return TypeFactory.defaultInstance().constructType(ConcreteClass1.class);
                        } else if (id.equals("2")) {
                            return TypeFactory.defaultInstance().constructType(ConcreteClass2.class);
                        }
                        return null;
                    }

                    @Override
                    public String getDescForKnownTypeIds() {
                        return null;
                    }

                    @Override
                    public JsonTypeInfo.Id getMechanism() {
                        return null;
                    }
                };
            }

            @Override
            public Class<?> getDefaultImpl() {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public String getPropertyName() {
                return null;
            }
        };

        TypeDeserializerBase.ObjectWrapper<Class<?>> baseTypeWrapper = new TypeDeserializerBase.ObjectWrapper<>(ConcreteClass1.class);
        TypeDeserializerBase.ObjectWrapper<BeanProperty> propertyWrapper = new TypeDeserializerBase.ObjectWrapper<>(null);
        JsonDeserializer<?> deser = deser._findDeserializer(null, "{type:\"1\"}", baseTypeWrapper, propertyWrapper);
        
        assertTrue(deser instanceof ConcreteClass1Deserializer);
    }

    private static class ConcreteClass1 {}
    private static class ConcreteClass2 {}

    private static class ConcreteClass1Deserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return null;
        }
    }
}