import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);

        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(Object.class);

        assertNotNull(narrowedType);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withContentType(String.class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType typeWithHandler = simpleType.withTypeHandler("handler");

        assertEquals(String.class, typeWithHandler.getRawClass());
        assertEquals(null, typeWithHandler.getTypeHandler());
        assertEquals("handler", simpleType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withContentTypeHandler("handler");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType typeWithValueHandler = simpleType.withValueHandler("handler");

        assertEquals(String.class, typeWithValueHandler.getRawClass());
        assertEquals(null, typeWithValueHandler.getValueHandler());
        assertEquals("handler", simpleType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withContentValueHandler("handler");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType typeWithStaticTyping = simpleType.withStaticTyping();

        assertEquals(String.class, typeWithStaticTyping.getRawClass());
        assertEquals(false, typeWithStaticTyping.isStatic());
        assertEquals(true, simpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);

        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class);
        String canonicalName = simpleType.buildCanonicalName();

        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);

        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);

        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEqualsWithSameObject() {
        SimpleType simpleType = new SimpleType(String.class);

        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEqualsWithNull() {
        SimpleType simpleType = new SimpleType(String.class);

        assertFalse(simpleType.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        SimpleType simpleType = new SimpleType(String.class);

        assertFalse(simpleType.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentSimpleType() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(Integer.class);

        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithSameSimpleType() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);

        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithDifferentBindings() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.create(new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return String.class;
            }

            @Override
            public boolean hasRawClass(Class<?> cls) {
                return false;
            }

            @Override
            public <T> T getValueHandler() {
                return null;
            }

            @Override
            public <T> T getTypeHandler() {
                return null;
            }

            @Override
            public boolean hasValueHandler() {
                return false;
            }

            @Override
            public boolean hasHandlers() {
                return false;
            }

            @Override
            public boolean isContainerType() {
                return false;
            }

            @Override
            public Class<?> containedType(int index) {
                return null;
            }

            @Override
            public JavaType getKeyType() {
                return null;
            }

            @Override
            public JavaType getContentType() {
                return null;
            }

            @Override
            public int containedTypeCount() {
                return 0;
            }

            @Override
            public boolean isCollectionLikeType() {
                return false;
            }

            @Override
            public boolean isMapLikeType() {
                return false;
            }

            @Override
            public boolean isAbstract() {
                return false;
            }

            @Override
            public boolean isThrowable() {
                return false;
            }

            @Override
            public boolean isArrayType() {
                return false;
            }

            @Override
            public boolean isEnumType() {
                return false;
            }

            @Override
            public boolean isInterface() {
                return false;
            }

            @Override
            public boolean isPrimitive() {
                return false;
            }

            @Override
            public boolean isFinal() {
                return false;
            }

            @Override
            public boolean isCollectionType() {
                return false;
            }

            @Override
            public boolean isConcrete() {
                return false;
            }

            @Override
            public boolean hasGenericTypes() {
                return false;
            }

            @Override
            public Object createInstance() {
                return null;
            }

            @Override
            public boolean isTypeOrSubTypeOf(Class<?> cls) {
                return false;
            }
        }), null, null);

        assertFalse(simpleType1.equals(simpleType2));
    }
}