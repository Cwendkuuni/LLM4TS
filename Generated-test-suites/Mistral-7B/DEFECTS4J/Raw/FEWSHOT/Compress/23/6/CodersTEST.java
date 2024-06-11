package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.SecretKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.compress.archivers.sevenz.Coders.CoderBase;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderId;
import org.apache.commons.compress.archivers.sevenz.Coders.CopyDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.DeflateDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.AES256SHA256Decoder;
import org.junit.Test;

public class CodersTest {

    @Test
    public void testAddDecoder() {
        byte[] password = "password".getBytes();
        InputStream is = new ByteArrayInputStream(new byte[]{-37, 0, 80, 75, 16, 1, 3, 141, 1, 3, 0, 5, 4, 0, 0, 0, 1});

        byte[] expected = new byte[]{-37, 0, 80, 75, 16, 1, 3, 141, 1, 3, 0, 5, 4, 0, 0, 0, 1};
        InputStream result = Coders.addDecoder(is, new CoderId(SevenZMethod.COPY, new CopyDecoder()), password);
        assertArrayEquals(expected, result.readAllBytes());

        expected = new byte[]{0, -26, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 12, 0, 12, 6, 0, 1};
        result = Coders.addDecoder(is, new CoderId(SevenZMethod.LZMA, new LZMADecoder()), password);
        assertArrayEquals(expected, result.readAllBytes());

        expected = new byte[]{-11, -75, 7, 19, -119, 118, -21, 48, -101, -112, 120, -116, 56, -126, 56,
                12, 122, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 122, 1, 1, 1, 1, 1, 0};
        result = Coders.addDecoder(is, new CoderId(SevenZMethod.BZIP2, new BZIP2Decoder()), password);
        assertArrayEquals(expected, result.readAllBytes());

        assertThrows(IOException.class, () -> Coders.addDecoder(is, new CoderId(SevenZMethod.DEFLATE, new DeflateDecoder()), password));

        expected = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87,
                88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111};
        result = Coders.addDecoder(is, new CoderId(SevenZMethod.AES256SHA256, new AES256SHA256Decoder()), password);
        assertArrayEquals(expected, result.readAllBytes());
    }

    @Test
    public void testAddEncoder() {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = "password".getBytes();

        byte[] input = new byte[]{-37, 0, 80, 75, 16, 1, 3, 141, 1, 3, 0, 5, 4, 0, 0, 0, 1};
        CoderId coderId = new CoderId(SevenZMethod.COPY, new CopyDecoder());
        try {
            Coders.addEncoder(out, coderId.method, password);
            out.write(input);
            out.close();
            byte[] result = out.toByteArray();
            assertEquals(input.length, result.length);
            assertArrayEquals(input, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        out = new ByteArrayOutputStream();
        try {
            Coders.addEncoder(out, SevenZMethod.LZMA, password);
            assertThrows(UnsupportedOperationException.class, () -> out.write(input));
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] salt = new byte[]{0, 1, 2, 3};
        byte[] iv = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        try {
            Coders.addEncoder(out, SevenZMethod.AES256SHA256, password);
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] aesKeyBytes = secretKey.getEncoded();
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKeyBytes, "AES"), new IvParameterSpec(iv));
            byte[] encrypted = cipher.doFinal(input);
            out.write(aesKeyBytes, 0, aesKeyBytes.length);
            out.write(iv, 0, iv.length);
            out.write(encrypted);
            out.close();
            byte[] result = out.toByteArray();
            assertEquals(input.length + aesKeyBytes.length + iv.length, result.length);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}