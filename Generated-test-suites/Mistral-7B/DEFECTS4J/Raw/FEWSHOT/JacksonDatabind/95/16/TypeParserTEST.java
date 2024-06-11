package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory;

    @Test
    public void testTypeParserInitialization() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        TypeParser parser = new TypeParser(_factory);
        assertEquals(_factory, parser.withFactory(new TypeFactory() {})._factory);
    }

    @Test
    public void testParseNull() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(null));
    }

    @Test
    public void testEmptyString() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return new SimpleType(String.class);
            }
        };
        JavaType result = new TypeParser(_factory).parse("");
        JavaType expected = _factory._fromClass(null, String.class, null);
        assertEquals(expected, result);
    }

    @Test
    public void testSimpleType() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return new SimpleType(String.class);
            }
        };
        JavaType result = new TypeParser(_factory).parse("String");
        JavaType expected = _factory._fromClass(null, String.class, null);
        assertEquals(expected, result);
    }

    @Test
    public void testSimpleTypeWithGenerics() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return new SimpleType(String.class);
            }
        };
        JavaType result = new TypeParser(_factory).parse("List<String>");
        JavaType expected = _factory._fromClass(ArrayList.class, List.class, Arrays.asList(String.class));
        assertEquals(expected, result);
    }

    @Test
    public void testTypeWithInvalidToken() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("List<String> #"));
    }

    @Test
    public void testTypeWithMissingClosingBracket() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("List<String>"));
    }

    @Test
    public void testTypeWithExtraClosingBracket() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("List<String> >"));
    }

    @Test
    public void testTypeWithInvalidClass() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("List<String> Foo"));
    }

    @Test
    public void testTypeWithInvalidClass2() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("List<String> List"));
    }

    @Test
    public void testTypeWithMissingComma() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("List<String> String int"));
    }

    @Test
    public void testTypeWithExtraComma() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("List<String> String, int"));
    }

    @Test
    public void testTypeWithMissingBracket() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("List<String> String int "));
    }

    @Test
    public void testTypeWithMissingBracket2() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("List<String> String int "));
    }

    @Test
    public void testTypeWithInvalidTokenInside() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("List<String> String # int"));
    }

    @Test
    public void testTypeWithValidType() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        JavaType result = new TypeParser(_factory).parse("List<String>");
        JavaType expected = _factory._fromClass(ArrayList.class, List.class, Arrays.asList(String.class));
        assertEquals(expected, result);
    }

    @Test
    public void testTypeWithValidTypeWithGenerics() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        JavaType result = new TypeParser(_factory).parse("List<List<Integer>>");
        JavaType expected = _factory._fromClass(ArrayList.class, List.class, Arrays.asList(List.class, Integer.class));
        assertEquals(expected, result);
    }

    @Test
    public void testTypeWithValidTypeWithInnerGenerics() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> clz, TypeBindings bindings) {
                return null;
            }
        };
        JavaType result = new TypeParser(_factory).parse("List<Map<String, List<Integer>>>");
        JavaType expected = _factory._fromClass(ArrayList.class, List.class, Arrays.asList(Map.class, String.class, List.class, Integer.class));
        assertEquals(expected, result);
    }
}