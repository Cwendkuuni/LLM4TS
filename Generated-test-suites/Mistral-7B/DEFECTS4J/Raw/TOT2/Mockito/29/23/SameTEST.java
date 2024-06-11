
package org.mockito.internal.matchers.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.*;

public class MatchesTest {

    @Test
    public void testSameObjects() {
        Same same = new Same(new Object());
        assertTrue(same.matches(new Object()));
    }

    @Test
    public void testDifferentObjects() {
        Same same = new Same(new Object());
        assertFalse(same.matches(new Object()[0]));
    }

    @Test
    public void testMatchesNull() {
        Same same = new Same(null);
        assertFalse(same.matches(new Object()));
        assertFalse(same.matches(null));
    }
}

public class DescribeToTest {

    @Test
    public void testDescriptionForString() {
        Object wanted = new Object();
        Description description = mock(Description.class);
        Same same = new Same(wanted);

        same.describeTo(description);
        String expected = "same(" + wanted.toString() + ")";
        assertThat(description.toString(), equalTo(expected));
    }

    @Test
    public void testDescriptionForCharacter() {
        char wanted = 'a';
        Description description = mock(Description.class);
        Same same = new Same(wanted);

        same.describeTo(description);
        String expected = "same('a')";
        assertThat(description.toString(), equalTo(expected));
    }
}