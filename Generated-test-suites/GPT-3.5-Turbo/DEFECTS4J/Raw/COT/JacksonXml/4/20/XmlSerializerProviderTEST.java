
package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue_NullValue_SerializeXmlNullCalled() {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        MockXmlGenerator generator = new MockXmlGenerator();
        provider.serializeValue(generator, null);
        assertTrue(generator.serializeXmlNullCalled);
    }

    @Test
    public void testSerializeValue_NonNullValue_StartRootArrayCalled() {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        MockXmlGenerator generator = new MockXmlGenerator();
        provider.serializeValue(generator, "test");
        assertTrue(generator.startRootArrayCalled);
    }

    private class MockXmlGenerator extends ToXmlGenerator {
        public boolean serializeXmlNullCalled;
        public boolean startRootArrayCalled;

        public MockXmlGenerator() {
            super(null, null);
        }

        public void serializeXmlNull() throws IOException {
            serializeXmlNullCalled = true;
        }

        public void startRootArray() throws IOException {
            startRootArrayCalled = true;
        }
    }
}

