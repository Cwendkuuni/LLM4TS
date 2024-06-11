package com.fasterxml.jackson.core;

import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testMatches() {
        // Test empty pointer
        JsonPointer pointer = JsonPointer.EMPTY;
        assert(pointer.matches());
        
        // Test non-empty pointer
        pointer = new JsonPointer("example", "foobar", JsonPointer.EMPTY);
        assert(!pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        // Test matching property
        JsonPointer pointer = new JsonPointer("example", "foo", JsonPointer.EMPTY);
        assert(pointer.getMatchingProperty().equals("foo"));
        
        // Test non-matching property
        pointer = new JsonPointer("example", "bar", JsonPointer.EMPTY);
        assert(pointer.getMatchingProperty().equals("bar"));
    }
    
    @Test
    public void testGetMatchingIndex() {
        // Test matching index
        JsonPointer pointer = new JsonPointer("example", "2", JsonPointer.EMPTY);
        assert(pointer.getMatchingIndex() == 2);
        
        // Test non-matching index
        pointer = new JsonPointer("example", "5", JsonPointer.EMPTY);
        assert(pointer.getMatchingIndex() == 5);
    }
    
    @Test
    public void testMayMatchProperty() {
        // Test matching property
        JsonPointer pointer = new JsonPointer("example", "foo", JsonPointer.EMPTY);
        assert(pointer.mayMatchProperty());
        
        // Test matching index
        pointer = new JsonPointer("example", "2", JsonPointer.EMPTY);
        assert(!pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        // Test matching index
        JsonPointer pointer = new JsonPointer("example", "2", JsonPointer.EMPTY);
        assert(pointer.mayMatchElement());
        
        // Test matching property
        pointer = new JsonPointer("example", "foo", JsonPointer.EMPTY);
        assert(!pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        // Test matching property
        JsonPointer pointer = new JsonPointer("example", "foo", new JsonPointer("foobar", "baz", JsonPointer.EMPTY));
        JsonPointer result = pointer.matchProperty("foo");
        assert(result != null);
        assert(result.getMatchingProperty().equals("baz"));
        assert(result.tail().matches());
        
        // Test non-matching property
        result = pointer.matchProperty("bar");
        assert(result == null);
    }
    
    @Test
    public void testMatchElement() {
        // Test matching index
        JsonPointer pointer = new JsonPointer("example", "2", new JsonPointer("foobar", "4", JsonPointer.EMPTY));
        JsonPointer result = pointer.matchElement(2);
        assert(result != null);
        assert(result.getMatchingIndex() == 4);
        assert(result.tail().matches());
        
        // Test non-matching index
        result = pointer.matchElement(5);
        assert(result == null);
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("example", "foo", new JsonPointer("foobar", "baz", JsonPointer.EMPTY));
        JsonPointer tail = pointer.tail();
        assert(tail != null);
        assert(tail.getMatchingProperty().equals("baz"));
        assert(tail.tail().matches());
    }
}
