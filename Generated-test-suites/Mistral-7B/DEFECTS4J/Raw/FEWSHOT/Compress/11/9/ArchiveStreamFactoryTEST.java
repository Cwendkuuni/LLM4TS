package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        for (String archiverName : ArchiveStreamFactory.values()) {
            InputStream testData = getClass().getResourceAsStream("/testdata/" + archiverName + ".test");
            ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, testData);
            assertNotNull(in);
            in.close();
        }

        InputStream testData = getClass().getResourceAsStream("/testdata/unknown.test");
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", testData);
        assertNull(in);

        InputStream testDataNull = null;
        ArchiveInputStream inNull = ArchiveStreamFactory.getInstance().createArchiveInputStream("tar", testDataNull);
        assertNull(inNull);
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        for (String archiverName : ArchiveStreamFactory.values()) {
            OutputStream testData = new ByteArrayOutputStream();
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, testData);
            assertNotNull(out);
            out.close();
        }

        OutputStream testDataNull = null;
        ArchiveOutputStream outNull = ArchiveStreamFactory.getInstance().createArchiveOutputStream("tar", testDataNull);
        assertNull(outNull);
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws IOException, ArchiveException {
        byte[] testDataAr = getResourceAsBytes("/testdata/ar.test");
        ArchiveInputStream inAr = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(testDataAr));
        assertNotNull(inAr);
        inAr.close();

        byte[] testDataCpio = getResourceAsBytes("/testdata/cpio.test");
        ArchiveInputStream inCpio = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(testDataCpio));
        assertNotNull(inCpio);
        inCpio.close();

        byte[] testDataZip = getResourceAsBytes("/testdata/zip.test");
        ArchiveInputStream inZip = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(testDataZip));
        assertNotNull(inZip);
        inZip.close();

        // Dump and Tar need bigger test files
        byte[] testDataDump = getResourceAsBytes("/testdata/dump.test");
        ArchiveInputStream inDump = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(testDataDump));
        assertNotNull(inDump);
        inDump.close();

        byte[] testDataTar = getResourceAsBytes("/testdata/tar.test");
        ArchiveInputStream inTar = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(testDataTar));
        assertNotNull(inTar);
        inTar.close();

        byte[] testDataUnknown = new byte[0];
        ArchiveInputStream inUnknown = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(testDataUnknown));
        assertNull(inUnknown);
    }

    @Test
    public void testMatchesSignature() throws IOException {
        for (String archiverName : ArchiveStreamFactory.values()) {
            byte[] signature = null;
            try (InputStream testData = getClass().getResourceAsStream("/testdata/" + archiverName + ".test")) {
                signature = new byte[testData.available()];
                testData.read(signature);
            }

            boolean matches = false;
            switch (archiverName) {
                case "ar":
                    matches = ArArchiveInputStream.matches(signature, signature.length);
                    break;
                case "zip":
                    matches = ZipArchiveInputStream.matches(signature, signature.length);
                    break;
                case "tar":
                    matches = TarArchiveInputStream.matches(signature, signature.length);
                    break;
                case "jar":
                    matches = JarArchiveInputStream.matches(signature, signature.length);
                    break;
                case "cpio":
                    matches = CpioArchiveInputStream.matches(signature, signature.length);
                    break;
                case "dump":
                    matches = DumpArchiveInputStream.matches(signature, signature.length);
                    break;
                default:
                    throw new AssertionError("Unknown archiverName: " + archiverName);
            }

            assertTrue(matches);
        }
    }

    private byte[] getResourceAsBytes(String path) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(path);
        byte[] byteArray = new byte[inputStream.available()];
        inputStream.read(byteArray);
        inputStream.close();
        return byteArray;
    }
}