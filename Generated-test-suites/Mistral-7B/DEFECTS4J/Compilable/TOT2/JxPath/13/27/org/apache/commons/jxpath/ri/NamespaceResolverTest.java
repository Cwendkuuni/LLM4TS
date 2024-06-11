package org.apache.commons.jxpath.ri;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testNamespaceResolverConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNotNull(resolver);
    }
}