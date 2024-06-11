package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAProperties;
import org.apache.commons.compress.utils.ByteStreams;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.tukaani.xz.LZMAPropertiesConstants;

import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.Coder;
import org.apache.commons.compress.archivers.sevenz.CoderId;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.CoderBase;

public class CodersTest {

 @Test
 public void testAddDecoder_Copy() throws IOException {
 InputStream is = new ByteArrayInputStream(new byte[] { 0x00, 0x01, 0x02 });
 Coder copyCoder = new CoderId(SevenZMethod.COPY, new CopyDecoder()).coder;
 InputStream decoder = Coders.addDecoder(is, copyCoder, null);
 assertEquals(is, decoder);
 }

 @Test
 public void testAddDecoder_UnsupportedMethod() throws IOException {
 InputStream is = new ByteArrayInputStream(new byte[] { 0x01 });
 IOException exception = assertThrows(IOException.class, () -> {
 Coders.addDecoder(is, new Object(), null);
 });
 assertEquals("Unsupported compression method [1]", exception.getMessage());
 }

 @Test
 public void testAddDecoder_LZMA() throws IOException, NoSuchAlgorithmException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 LZMAOutputStream lzmaOutputStream = new LZMAOutputStream(outputStream);
 lzmaOutputStream.setProperties(new LZMAProperties() {{
 setDictionarySize(LZMAPropertiesConstants.LZMA_PROPS_DICT_SIZE_AUTO);
 setProps(new byte[] { 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 });
 }});
 lzmaOutputStream.write(new byte[] { 0x01, 0x02, 0x03 });
 lzmaOutputStream.finish();
 InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
 Coder lzmaCoder = new CoderId(SevenZMethod.LZMA, new LZMADecoder()).coder;
 InputStream decoder = Coders.addDecoder(is, lzmaCoder, null);
 byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
 byte[] actual = IOUtils.toByteArray(decoder);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testAddDecoder_LZMA_InvalidDictionarySize() throws IOException, NoSuchAlgorithmException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 LZMAOutputStream lzmaOutputStream = new LZMAOutputStream(outputStream);
 lzmaOutputStream.setProperties(new LZMAProperties() {{
 setDictionarySize(Integer.MAX_VALUE);
 setProps(new byte[] { 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 });
 }});
 lzmaOutputStream.write(new byte[] { 0x01, 0x02, 0x03 });
 lzmaOutputStream.finish();
 InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
 Coder lzmaCoder = new CoderId(SevenZMethod.LZMA, new LZMADecoder()).coder;
 IOException exception = assertThrows(IOException.class, () -> {
 Coders.addDecoder(is, lzmaCoder, null);
 });
 assertEquals("Dictionary larger than 4GiB maximum size", exception.getMessage());
 }

 @Test
 public void testAddEncoder_Copy() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.COPY, null);
 outputStream.write(new byte[] { 0x01, 0x02, 0x03 });
 outputStream.flush();
 outputStream.close();
 byte[] actual = IOUtils.toByteArray(Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()),
 new CoderId(SevenZMethod.COPY, new CopyDecoder()).coder, null));
 byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testAddEncoder_UnsupportedMethod() throws IOException {
 byte[] data = new byte[] { 0x01, 0x02, 0x03 };
 IOException exception = assertThrows(IOException.class, () -> {
 Coders.addEncoder(new ByteArrayOutputStream(), SevenZMethod.LZMA2, null);
 });
 assertEquals("Unsupported compression method LZMA2", exception.getMessage());
 }

 @Test
 public void testAddEncoder_BZIP2() throws IOException, NoSuchAlgorithmException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.BZIP2, null);
 encoder.write(new byte[] { 0x01, 0x02, 0x03 });
 encoder.finish();
 OutputStream decoder = new BZip2CompressorOutputStream(new ByteArrayOutputStream());
 decoder.write(outputStream.toByteArray());
 decoder.finish();
 InputStream is = new BZip2CompressorInputStream(decoder);
 byte[] actual = IOUtils.toByteArray(is);
 byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testAddEncoder_AES256SHA256() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10 });
 encoder.write(new byte[] { 0x01, 0x02, 0x03 });
 encoder.finish();
 InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
 Coder aes256sha256Coder = new CoderId(SevenZMethod.AES256SHA256, new AES256SHA256Decoder()).coder;
 InputStream decoder = Coders.addDecoder(is, aes256sha256Coder, new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10 });
 byte[] actual = IOUtils.toByteArray(decoder);
 byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
 assertArrayEquals(expected, actual);
 }
}