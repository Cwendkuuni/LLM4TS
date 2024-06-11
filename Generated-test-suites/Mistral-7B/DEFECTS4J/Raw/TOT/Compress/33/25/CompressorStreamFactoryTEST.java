package org.apache.commons.compress.compressors.test;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testGzipCompressorCreation() throws Exception {
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        CompressorOutputStream output = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, new FileOutputStream("output.gz"));
        IOUtils.copy(input, output);
        output.close();

        InputStream testInput = new FileInputStream("output.gz");
        CompressorInputStream testOutput = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, testInput);
        byte[] expected = "Test data".getBytes();
        byte[] actual = new byte[expected.length];
        IOUtils.readFully(testOutput, actual);
        testOutput.close();
        FileUtils.deleteQuietly(new File("output.gz"));
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBzip2CompressorCreation() throws Exception {
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        CompressorOutputStream output = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, new FileOutputStream("output.bz2"));
        IOUtils.copy(input, output);
        output.close();

        InputStream testInput = new FileInputStream("output.bz2");
        CompressorInputStream testOutput = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, testInput);
        byte[] expected = "Test data".getBytes();
        byte[] actual = new byte[expected.length];
        IOUtils.readFully(testOutput, actual);
        testOutput.close();
        FileUtils.deleteQuietly(new File("output.bz2"));
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testXZCompressorCreation() throws Exception {
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        CompressorOutputStream output = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ, new FileOutputStream("output.xz"));
        IOUtils.copy(input, output);
        output.close();

        InputStream testInput = new FileInputStream("output.xz");
        CompressorInputStream testOutput = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, testInput);
        byte[] expected = "Test data".getBytes();
        byte[] actual = new byte[expected.length];
        IOUtils.readFully(testOutput, actual);
        testOutput.close();
        FileUtils.deleteQuietly(new File("output.xz"));
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDecompressorCreation() throws Exception {
        byte[] input = "Test data".getBytes();
        byte[] compressedInput = null;

        // Gzip
        InputStream gzipInput = new ByteArrayInputStream(input);
        OutputStream gzipOutput = new FileOutputStream("output.gz");
        CompressorOutputStream gzipCompressorOutput = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, gzipOutput);
        IOUtils.copy(gzipInput, gzipCompressorOutput);
        gzipOutput.close();
        gzipCompressorOutput.close();

        compressedInput = IOUtils.toByteArray(new FileInputStream("output.gz"));
        InputStream gzipTestInput = new ByteArrayInputStream(compressedInput);
        CompressorInputStream gzipTestOutput = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipTestInput);
        byte[] expected = "Test data".getBytes();
        byte[] actual = new byte[expected.length];
        IOUtils.readFully(gzipTestOutput, actual);
        gzipTestOutput.close();
        IOUtils.closeQuietly(new FileInputStream("output.gz"));
        assertArrayEquals(expected, actual);

        // Bzip2
        InputStream bzip2Input = new ByteArrayInputStream(input);
        OutputStream bzip2Output = new FileOutputStream("output.bz2");
        CompressorOutputStream bzip2CompressorOutput = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, bzip2Output);
        IOUtils.copy(bzip2Input, bzip2CompressorOutput);
        bzip2Output.close();
        bzip2CompressorOutput.close();

        compressedInput = IOUtils.toByteArray(new FileInputStream("output.bz2"));
        InputStream bzip2TestInput = new ByteArrayInputStream(compressedInput);
        CompressorInputStream bzip2TestOutput = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, bzip2TestInput);
        IOUtils.readFully(bzip2TestOutput, actual);
        bzip2TestOutput.close();
        IOUtils.closeQuietly(new FileInputStream("output.bz2"));
        assertArrayEquals(expected, actual);

        // XZ
        InputStream xzInput = new ByteArrayInputStream(input);
        OutputStream xzOutput = new FileOutputStream("output.xz");
        CompressorOutputStream xzCompressorOutput = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ, xzOutput);
        IOUtils.copy(xzInput, xzCompressorOutput);
        xzOutput.close();
        xzCompressorOutput.close();

        compressedInput = IOUtils.toByteArray(new FileInputStream("output.xz"));
        InputStream xzTestInput = new ByteArrayInputStream(compressedInput);
        CompressorInputStream xzTestOutput = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, xzTestInput);
        IOUtils.readFully(xzTestOutput, actual);
        xzTestOutput.close();
        IOUtils.closeQuietly(new FileInputStream("output.xz"));
        assertArrayEquals(expected, actual);

        // Deflate
        InputStream deflateInput = new ByteArrayInputStream(input);
        OutputStream deflateOutput = new FileOutputStream("output.zip");
        CompressorOutputStream deflateCompressorOutput = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, deflateOutput);
        IOUtils.copy(deflateInput, deflateCompressorOutput);
        deflateOutput.close();
        deflateCompressorOutput.close();

        compressedInput = IOUtils.toByteArray(new FileInputStream("output.zip"));
        InputStream deflateTestInput = new ByteArrayInputStream(compressedInput);
        CompressorInputStream deflateTestOutput = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, deflateTestInput);
        IOUtils.readFully(deflateTestOutput, actual);
        deflateTestOutput.close();
        IOUtils.closeQuietly(new FileInputStream("output.zip"));
        assertArrayEquals(expected, actual);

        // Reset input
        gzipInput = new ByteArrayInputStream(input);
        bzip2Input = new ByteArrayInputStream(input);
        xzInput = new ByteArrayInputStream(input);
        deflateInput = new ByteArrayInputStream(input);
    }

    @Test(expected = CompressorException.class)
    public void testUnknownCompressorCreation() throws Exception {
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        new CompressorStreamFactory()
                .createCompressorOutputStream("Unknown", new FileOutputStream("output.unknown"));
    }
}