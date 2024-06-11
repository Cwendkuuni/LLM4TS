package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory.DecompressOption;

import org.junit.Test;

/**
 * Unit tests for CompressorStreamFactory class
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateFactoryWithoutOptions() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());
    }

    @Test
    public void testCreateFactoryWithDecompressConcatenatedTrue() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateFactoryWithDecompressConcatenatedTwice() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.setDecompressConcatenated(true);
        factory.setDecompressConcatenated(false);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateFactoryWithDecompressUntilEOF() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        factory.setDecompressConcatenated(false);
        factory.setDecompressUntilEOF(true);
    }
}