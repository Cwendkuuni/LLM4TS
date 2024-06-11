package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test cases for DatabindContext class.
 */
public class DatabindContextTest {

    // Test Util methods

    @Test
    public void testGetConfig() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public MapperConfig<?> getConfig() {
                return new MapperConfig.Builder().build();
            }
        };
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return new AnnotationIntrospector() {
                    @Override
                    public ObjectIdInfo findIdInfo(AnnotatedMember member, TypeSerializer vfs, BeanDescription beanDesc, JsonMappingException mc) throws JsonMappingException {
                        return null;
                    }
                };
            }
        };
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        assertNotNull(annotationIntrospector);
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public boolean isEnabled(MapperFeature feature) {
                return true;
            }
        };
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_INTERNAL_DATE_FORMAT));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public boolean canOverrideAccessModifiers() {
                return true;
            }
        };
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public Class<?> getActiveView() {
                return null;
            }
        };
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public Locale getLocale() {
                return Locale.ENGLISH;
            }
        };
        assertEquals(Locale.ENGLISH, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public TimeZone getTimeZone() {
                return TimeZone.getDefault();
            }
        };
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testgetDefaultPropertyFormat() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
                return null;
            }
        };
        assertNull(context.getDefaultPropertyFormat(String.class));
    }

    // Test getAttribute and setAttribute

    @Test
    public void testGetAttribute() {
        DatabindContext context = new BaseDatabindContext() {
            private int counter = 0;

            @Override
            public Object getAttribute(Object key) {
                if (key == null) {
                    throw new NullPointerException();
                }
                return ++counter;
            }
        };
        DatabindContext newContext = context.setAttribute(new Object(), 100);
        assertEquals(1, context.getAttribute(new Object()));
        assertEquals(101, newContext.getAttribute(new Object()));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAttributeNullKey() {
        DatabindContext context = new BaseDatabindContext();
        context.getAttribute(null);
    }

    // Test Type instantiation/resolution

    @Test
    public void testConstructType() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public TypeFactory getTypeFactory() {
                return new TypeFactory() {
                    @Override
                    public JavaType constructType(Type type) {
                        if (type == null) {
                            return null;
                        }
                        return new SimpleType(ClassUtil.getRawClass(type));
                    }
                };
            }
        };
        JavaType javaType = context.constructType(String.class);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public JavaType getTypeFactory() {
                return new TypeFactory() {
                    @Override
                    public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
                        if (baseType == null || subclass == null) {
                            return null;
                        }
                        return new SimpleType(ClassUtil.getRawClass(subclass));
                    }
                };
            }
        };
        JavaType javaType = context.constructType(String.class);
        JavaType specializedType = context.constructSpecializedType(javaType, Map.class);
        assertNotNull(specializedType);
        assertEquals(Map.class, specializedType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeNullBaseType() {
        DatabindContext context = new BaseDatabindContext();
        context.resolveSubType(null, "java.lang.String");
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeInvalidTypeId() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
                throw new JsonMappingException("Test");
            }
        };
        context.resolveSubType(new SimpleType(int.class), "java.lang.String");
    }

    @Test
    public void testResolveSubType() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
                throw new JsonMappingException("Test");
            }

            @Override
            public JavaType constructType(Type type) {
                if (type == null) {
                    return null;
                }
                return new SimpleType(ClassUtil.getRawClass(type));
            }

            @Override
            public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
                if (baseType == null || subclass == null) {
                    return null;
                }
                return new SimpleType(ClassUtil.getRawClass(subclass));
            }

            @Override
            public JavaType resolveSubType(JavaType baseType, String subClass)
                    throws JsonMappingException {
                if (baseType == null || subClass == null) {
                    return null;
                }
                if (subClass.equals("java.lang.String")) {
                    return constructType(String.class);
                }
                if (subClass.equals("java.util.Map")) {
                    return constructSpecializedType(baseType, Map.class);
                }
                throw new JsonMappingException("Type not found: " + subClass);
            }
        };
        JavaType javaType = context.resolveSubType(new SimpleType(List.class), "java.util.Map");
        assertNotNull(javaType);
        assertEquals(Map.class, javaType.getRawClass());
    }

    // Test error reporting

    @Test
    public void testReportBadDefinitionNullType() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
                throw new RuntimeException("Should not reach here");
            }
        };
        try {
            context.reportBadDefinition(null, "Test error");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReportBadDefinitionNullMsg() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
                throw new RuntimeException("Should not reach here");
            }
        };
        try {
            context.reportBadDefinition(new SimpleType(String.class), null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReportBadDefinition() {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
                throw new RuntimeException("Test");
            }
        };
        try {
            context.reportBadDefinition(new SimpleType(String.class), "Test error");
            fail();
        } catch (RuntimeException e) {
            // expected
        }
    }
}

class BaseDatabindContext extends DatabindContext {
    // empty implementation
}

class SimpleType extends JavaType {
    private final Class<?> _class;

    SimpleType(Class<?> rawClass) {
        super(TypeFactory.defaultInstance(), rawClass, null, null);
        _class = rawClass;
    }

    @Override
    public Class<?> getRawClass() {
        return _class;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[type id=" + _class.getName() + "]";
    }
}