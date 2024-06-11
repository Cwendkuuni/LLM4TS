package com.fasterxml.jackson.core;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assert (pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assert (pointer.getMatchingProperty().equals(""));
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assert (pointer.getMatchingIndex() == -1);
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assert (pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assert (pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assert (pointer.matchProperty("name") == null); // pointer is empty so it should not match anything
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assert (pointer.matchElement(0) == null); // pointer is empty so it should not match anything
    }

    @Test
    public void testTail() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = pointer1.tail();
        assert (pointer2.matches());
        assert (pointer2.getMatchingProperty().equals(""));
        assert (pointer2.getMatchingIndex() == -1);
        assert (!pointer2.mayMatchProperty());
        assert (!pointer2.mayMatchElement());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assert (pointer.toString().equals(""));
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assert (pointer1.hashCode() == pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assert (pointer1.equals(pointer2));
    }
}