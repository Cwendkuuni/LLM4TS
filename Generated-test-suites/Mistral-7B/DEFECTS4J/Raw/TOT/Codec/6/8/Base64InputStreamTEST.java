import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamDecoding() throws IOException {
        // Test decoding a Base64-encoded stream with no line breaks
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int read;
        byte[] result = new byte[inputStream.available()];
        while ((read = base64InputStream.read(result, 0, result.length)) != -1) {
            outputStream.write(result, 0, read);
        }
        outputStream.flush();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/".getBytes(), outputStream.toByteArray());

        // Test decoding a Base64-encoded stream with line breaks
        String base64EncodedDataWithLineBreaks = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\nVG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        inputStream = new ByteArrayInputStream(base64EncodedDataWithLineBreaks.getBytes());
        outputStream = new ByteArrayOutputStream();
        base64InputStream = new Base64InputStream(inputStream, false, -1, null);
        result = new byte[inputStream.available()];
        while ((read = base64InputStream.read(result, 0, result.length)) != -1) {
            outputStream.write(result, 0, read);
        }
        outputStream.flush();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/".getBytes(), outputStream.toByteArray());

        // Test decoding a Base64-encoded stream with incorrect line breaks
        String base64EncodedDataWithIncorrectLineBreaks = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\rIVG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        inputStream = new ByteArrayInputStream(base64EncodedDataWithIncorrectLineBreaks.getBytes());
        outputStream = new ByteArrayOutputStream();
        base64InputStream = new Base64InputStream(inputStream, false, -1, null);
        result = new byte[inputStream.available()];
        while ((read = base64InputStream.read(result, 0, result.length)) != -1) {
            outputStream.write(result, 0, read);
        }
        outputStream.flush();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/".getBytes(), outputStream.toByteArray());

        // Test decoding a Base64-encoded stream that is too long
        String base64EncodedDataTooLong = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\nVG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        inputStream = new ByteArrayInputStream(base64EncodedDataTooLong.getBytes());
        outputStream = new ByteArrayOutputStream();
        base64InputStream = new Base64InputStream(inputStream, false, -1, null);
        result = new byte[inputStream.available()];
        int bytesRead = 0;
        while ((bytesRead += base64InputStream.read(result, bytesRead, result.length - bytesRead)) != -1) {
            outputStream.write(result, 0, bytesRead + bytesRead);
        }
        outputStream.flush();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/".getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        // Test encoding a byte array into a Base64-encoded stream
        byte[] inputData = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputData), true);
        int read;
        byte[] result = new byte[inputData.length];
        while ((read = base64InputStream.read(result, 0, result.length)) != -1) {
            base64OutputStream.write(result, 0, read);
        }
        base64OutputStream.finish();
        outputStream.flush();
        String base64EncodedData = new String(outputStream.toByteArray());
        Assert.assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".replaceAll("=", ""), base64EncodedData);

        // Test encoding a byte array into a Base64-encoded stream with line breaks
        ByteArrayOutputStream outputStreamWithLineBreaks = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStreamWithLineBreaks = new Base64OutputStream(outputStreamWithLineBreaks, false, 76, "\r\n".getBytes());
        Base64InputStream base64InputStreamWithLineBreaks = new Base64InputStream(new ByteArrayInputStream(inputData), true, 76, "\r\n".getBytes());
        int readWithLineBreaks;
        byte[] resultWithLineBreaks = new byte[inputData.length];
        while ((readWithLineBreaks = base64InputStreamWithLineBreaks.read(resultWithLineBreaks, 0, resultWithLineBreaks.length)) != -1) {
            base64OutputStreamWithLineBreaks.write(resultWithLineBreaks, 0, readWithLineBreaks);
        }
        base64OutputStreamWithLineBreaks.finish();
        outputStreamWithLineBreaks.flush();
        String base64EncodedDataWithLineBreaks = new String(outputStreamWithLineBreaks.toByteArray());
        Assert.assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\nVG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".replaceAll("=", ""), base64EncodedDataWithLineBreaks);

        // Test encoding a byte array into a Base64-encoded stream with incorrect line breaks
        ByteArrayOutputStream outputStreamWithIncorrectLineBreaks = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStreamWithIncorrectLineBreaks = new Base64OutputStream(outputStreamWithIncorrectLineBreaks, false, 76, "\rIV\n".getBytes());
        Base64InputStream base64InputStreamWithIncorrectLineBreaks = new Base64InputStream(new ByteArrayInputStream(inputData), true, 76, "\rIV\n".getBytes());
        int readWithIncorrectLineBreaks;
        byte[] resultWithIncorrectLineBreaks = new byte[inputData.length];
        while ((readWithIncorrectLineBreaks = base64InputStreamWithIncorrectLineBreaks.read(resultWithIncorrectLineBreaks, 0, resultWithIncorrectLineBreaks.length)) != -1) {
            base64OutputStreamWithIncorrectLineBreaks.write(resultWithIncorrectLineBreaks, 0, readWithIncorrectLineBreaks);
        }
        base64OutputStreamWithIncorrectLineBreaks.finish();
        outputStreamWithIncorrectLineBreaks.flush();
        String base64EncodedDataWithIncorrectLineBreaks = new String(outputStreamWithIncorrectLineBreaks.toByteArray());
        Assert.assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\rIVVG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".replaceAll("=", ""), base64EncodedDataWithIncorrectLineBreaks);

        // Test encoding a byte array that is too long into a Base64-encoded stream
        byte[] inputDataTooLong = new byte[128];
        ByteArrayOutputStream outputStreamTooLong = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStreamTooLong = new Base64OutputStream(outputStreamTooLong, false);
        Base64InputStream base64InputStreamTooLong = new Base64InputStream(new ByteArrayInputStream(inputDataTooLong), true);
        int readTooLong;
        byte[] resultTooLong = new byte[inputDataTooLong.length];
        while ((readTooLong = base64InputStreamTooLong.read(resultTooLong, 0, resultTooLong.length)) != -1) {
            base64OutputStreamTooLong.write(resultTooLong, 0, readTooLong);
        }
        base64OutputStreamTooLong.finish();
        outputStreamTooLong.flush();
        String base64EncodedDataTooLong = new String(outputStreamTooLong.toByteArray());
        Assert.assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\nVG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\nVG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\nVG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".replaceAll("=", ""), base64EncodedDataTooLong);
    }
}