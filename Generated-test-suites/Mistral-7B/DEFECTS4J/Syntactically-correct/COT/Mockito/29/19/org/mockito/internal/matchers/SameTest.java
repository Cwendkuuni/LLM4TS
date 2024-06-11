package org.mockito.internal.matchers.test;

import org.hamcrest.Description;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.matchers.Same;

import java.util.ArrayList;

public class SameTest {

    @Test
    public void testSameWithSameObjects() {
        Same same = new Same(new Object());
        Object actual = new Object();

        Assert.assertTrue(same.matches(actual));
    }

    @Test
    public void testSameWithNullAndNull() {
        Same same = new Same(null);
        Object actual = null;

        Assert.assertTrue(same.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testSameWithDifferentObjects() {
        Same same = new Same(new Object());
        Object actual = new ArrayList<Object>();

        Assert.assertFalse(same.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testSameWithNullAndDifferentObject() {
        Same same = new Same(new Object());
        Object actual = new ArrayList<Object>();

        Assert.assertFalse(same.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testSameWithDifferentNullAndDifferentObject() {
        Same same = new Same(null);
        Object actual = new ArrayList<Object>();

        Assert.assertFalse(same.matches(actual));
    }

    @Test
    public void testSameWithSameStrings() {
        Same same = new Same("test");
        String actual = "test";

        Assert.assertTrue(same.matches(actual));
    }

    @Test
    public void testSameWithSameCharacters() {
        Same same = new Same('a');
        Character actual = 'a';

        Assert.assertTrue(same.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testSameWithDifferentStrings() {
        Same same = new Same("test");
        String actual = "different";

        Assert.assertFalse(same.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testSameWithDifferentCharacters() {
        Same same = new Same('a');
        Character actual = 'b';

        Assert.assertFalse(same.matches(actual));
    }

    @Test
    public void testSameWithSameArrays() {
        Object[] wanted = new Object[1];
        Object[] actual = new Object[1];
        Same same = new Same(wanted);

        Assert.assertTrue(same.matches(actual));
    }

    @Test
    public void testSameWithSameCollections() {
        ArrayList<Object> wanted = new ArrayList<>();
        ArrayList<Object> actual = new ArrayList<>();

        Same same = new Same(wanted);

        Assert.assertTrue(same.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testSameWithDifferentArrays() {
        Object[] wanted = new Object[1];
        Object[] actual = new Object[2];

        Same same = new Same(wanted);

        Assert.assertFalse(same.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testSameWithDifferentCollections() {
        ArrayList<Object> wanted = new ArrayList<>();
        ArrayList<Object> actual = new ArrayList<>();
        wanted.add(new Object());
        actual.add(new Object());

        Same same = new Same(wanted);

        Assert.assertFalse(same.matches(actual));
    }

    @Test
    public void testSameWithSameInstances() {
        Object wanted = new Object();
        Object actual = wanted;

        Same same = new Same(wanted);

        Assert.assertTrue(same.matches(actual));
    }

    @Test
    public void testSameWithDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();

        Same same = new Same(wanted);

        Assert.assertFalse(same.matches(actual));
    }

    @Test
    public void testSameWithSameClassAndDifferentInstances() {
        String wanted = "test";
        String actual = new String("test");

        Same same = new Same(wanted);

        Assert.assertTrue(same.matches(actual));
    }

    @Test
    public void testSameWithSameClassAndSameObjects() {
        String wanted = "test";
        String actual = "test";

        Same same = new Same(wanted);

        Assert.assertTrue(same.matches(actual));
    }

    @Test
    public void testSameWithDifferentClassButSameObjects() {
        String wanted = "test";
        Object actual = "test";

        Same same = new Same(wanted);

        Assert.assertFalse(same.matches(actual));
    }
}