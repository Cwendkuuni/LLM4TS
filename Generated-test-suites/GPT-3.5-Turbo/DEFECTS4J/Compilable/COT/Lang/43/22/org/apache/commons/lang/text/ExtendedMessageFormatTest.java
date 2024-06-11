package org.apache.commons.lang.text;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}");
        assertNotNull(emf);
    }
}