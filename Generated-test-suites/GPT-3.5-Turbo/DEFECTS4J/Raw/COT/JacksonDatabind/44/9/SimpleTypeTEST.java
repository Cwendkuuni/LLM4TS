import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe_withInvalidMapClass_shouldThrowIllegalArgumentException() {
        try {
            SimpleType.constructUnsafe(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }

    @Test
    public void testConstructUnsafe_withInvalidCollectionClass_shouldThrowIllegalArgumentException() {
        try {
            SimpleType.constructUnsafe(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
    }

    @Test
    public void testConstructUnsafe_withInvalidArrayClass_shouldThrowIllegalArgumentException() {
        try {
            SimpleType.constructUnsafe(String[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testConstruct_withInvalidMapClass_shouldThrowIllegalArgumentException() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }

    @Test
    public void testConstruct_withInvalidCollectionClass_shouldThrowIllegalArgumentException() {
        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
    }

    @Test
    public void testConstruct_withInvalidArrayClass_shouldThrowIllegalArgumentException() {
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testWithContentType_shouldThrowIllegalArgumentException() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentType(Object.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler_shouldThrowIllegalArgumentException() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentTypeHandler(handler);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler_shouldThrowIllegalArgumentException() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentValueHandler(handler);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentValueHandler()", e.getMessage());
        }
    }

    // Helper value and type handlers for the tests
    private final Object handler = new Object() {
        // ...
    };
}