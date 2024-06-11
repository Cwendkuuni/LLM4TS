package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test case for CompressorStreamFactory class
 */
public class CompressorStreamFactoryTest {

    

    @org.junit.Test
    public void testCreateCompressorInputStream_gzip() throws IOException, CompressorException {
        final InputStream input = new ByteArrayInputStream("Gzip test data".getBytes());
        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertNotNull(in);
        IOUtils.closeQuietly(in);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_bzip2() throws IOException, CompressorException {
        final InputStream input = new ByteArrayInputStream("Bzip2 test data".getBytes());
        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        assertNotNull(in);
        IOUtils.closeQuietly(in);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_pack200() throws IOException, CompressorException {
        final InputStream input = new ByteArrayInputStream("Pack200 test data".getBytes());
        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.PACK200, input);
        assertNotNull(in);
        IOUtils.closeQuietly(in);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_xz() throws IOException, CompressorException {
        final InputStream input = new ByteArrayInputStream("XZ test data".getBytes());
        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.XZ, input);
        assertNotNull(in);
        IOUtils.closeQuietly(in);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_lzma() throws IOException, CompressorException {
        final InputStream input = new ByteArrayInputStream("LZMA test data".getBytes());
        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.LZMA, input);
        assertNotNull(in);
        IOUtils.closeQuietly(in);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_snappy_raw() throws IOException, CompressorException {
        final InputStream input = new ByteArrayInputStream("Snappy raw test data".getBytes());
        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.SNAPPY_RAW, input);
        assertNotNull(in);
        IOUtils.closeQuietly(in);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_snappy_framed() throws IOException, CompressorException {
        final InputStream input = new ByteArrayInputStream("Snappy framed test data".getBytes());
        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.SNAPPY_FRAMED, input);
        assertNotNull(in);
        IOUtils.closeQuietly(in);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_z() throws IOException, CompressorException {
        final InputStream input = new ByteArrayInputStream("Z test data".getBytes());
        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.Z, input);
        assertNotNull(in);
        IOUtils.closeQuietly(in);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_deflate() throws IOException, CompressorException {
        final InputStream input = new ByteArrayInputStream("Deflate test data".getBytes());
        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.DEFLATE, input);
        assertNotNull(in);
        IOUtils.closeQuietly(in);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_unknown() throws IOException {
        final InputStream input = new ByteArrayInputStream("Unknown test data".getBytes());
        new CompressorStreamFactory().createCompressorInputStream("unknown", input);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_nullInputStream() {
        new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, null);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_gzip() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream out = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertNotNull(out);
        IOUtils.closeQuietly(out);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_bzip2() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream out = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        assertNotNull(out);
        IOUtils.closeQuietly(out);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_pack200() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream out = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.PACK200, output);
        assertNotNull(out);
        IOUtils.closeQuietly(out);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_xz() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream out = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.XZ, output);
        assertNotNull(out);
        IOUtils.closeQuietly(out);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_lzma() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream out = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.LZMA, output);
        assertNotNull(out);
        IOUtils.closeQuietly(out);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_snappy_raw() {
        final OutputStream output = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.SNAPPY_RAW, output);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_snappy_framed() {
        final OutputStream output = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.SNAPPY_FRAMED, output);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_z() {
        final OutputStream output = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.Z, output);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_deflate() {
        final OutputStream output = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.DEFLATE, output);
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateCompressorOutputStream_nullOutputStream() {
        new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
    }

    @org.junit.Test
    public void testGetDecompressConcatenated() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNull(factory.getDecompressConcatenated());

        final CompressorStreamFactory factoryWithConcatenated = new CompressorStreamFactory(true);
        assertNotNull(factoryWithConcatenated.getDecompressConcatenated());
    }

    
}