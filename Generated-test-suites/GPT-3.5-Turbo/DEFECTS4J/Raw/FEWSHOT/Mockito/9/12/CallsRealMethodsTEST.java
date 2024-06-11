
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        Object mock = mock(Object.class, answer);

        // Stubbed method
        assertNotNull(mock.toString());

        // Unstubbed methods
        assertNull(mock.hashCode());
        assertEquals(false, mock.equals(null));

        // Verify that callRealMethod is invoked
        assertEquals(mock, mock.clone());
    }

}
