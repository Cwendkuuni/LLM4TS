import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.IOUtils;

public class IOUtilsTest {
    
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    
    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[0]);
    }
    
    @After
    public void tearDown() {
        try {
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Expert 1
    @Test
    public void testCopy_DefaultBufferSize() {
        try {
            byte[] testData = new byte[1000];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) (i % 256);
            }
            
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            IOUtils.copy(input, outputStream);
            
            byte[] outputData = outputStream.toByteArray();
            
            assertArrayEquals(testData, outputData);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 2
    @Test
    public void testCopy_CustomBufferSize() {
        try {
            byte[] testData = new byte[1000];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) (i % 256);
            }
            
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            IOUtils.copy(input, outputStream, 100);
            
            byte[] outputData = outputStream.toByteArray();
            
            assertArrayEquals(testData, outputData);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 3
    @Test
    public void testCopy_LargeBufferSize() {
        try {
            byte[] testData = new byte[1000];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) (i % 256);
            }
            
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            IOUtils.copy(input, outputStream, 1000000);
            
            byte[] outputData = outputStream.toByteArray();
            
            assertArrayEquals(testData, outputData);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 1
    @Test
    public void testSkip_EnoughBytesToSkip() {
        try {
            byte[] testData = new byte[100];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) (i % 256);
            }
            
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            IOUtils.skip(input, 50);
            assertEquals(50, input.available());
            
            input.read();
            assertEquals(49, input.available());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 2
    @Test
    public void testSkip_NotEnoughBytesToSkip() {
        try {
            byte[] testData = new byte[100];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) (i % 256);
            }
            
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            IOUtils.skip(input, 150);
            assertEquals(0, input.available());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 3
    @Test
    public void testSkip_ZeroBytesToSkip() {
        try {
            byte[] testData = new byte[100];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) (i % 256);
            }
            
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            IOUtils.skip(input, 0);
            assertEquals(100, input.available());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 1
    @Test
    public void testReadFully_EnoughBytesToRead() {
        try {
            byte[] testData = new byte[100];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) (i % 256);
            }
            
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            byte[] outputData = new byte[testData.length];
            
            int readBytes = IOUtils.readFully(input, outputData);
            
            assertEquals(testData.length, readBytes);
            assertArrayEquals(testData, outputData);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 2
    @Test
    public void testReadFully_NotEnoughBytesToRead() {
        try {
            byte[] testData = new byte[100];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) (i % 256);
            }
            
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            byte[] outputData = new byte[50];
            
            int readBytes = IOUtils.readFully(input, outputData);
            
            assertEquals(50, readBytes);
            
            byte[] expectedData = new byte[50];
            System.arraycopy(testData, 0, expectedData, 0, 50);
            assertArrayEquals(expectedData, outputData);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 3
    @Test
    public void testReadFully_ZeroBytesToRead() {
        try {
            byte[] testData = new byte[100];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) (i % 256);
            }
            
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            byte[] outputData = new byte[0];
            
            int readBytes = IOUtils.readFully(input, outputData);
            
            assertEquals(0, readBytes);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 1
    @Test
    public void testToByteArray() {
        try {
            byte[] testData = new byte[100];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) (i % 256);
            }
            
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            byte[] outputData = IOUtils.toByteArray(input);
            
            assertArrayEquals(testData, outputData);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 2
    @Test
    public void testCloseQuietly_CloseableNotNull() {
        try {
            IOUtils.closeQuietly(outputStream);
            assertTrue(outputStream.isClosed());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 3
    @Test
    public void testCloseQuietly_CloseableNull() {
        try {
            IOUtils.closeQuietly(null);
            assertTrue(true); // No exception should be thrown
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 1
    @Test
    public void testCloseQuietly_IOExceptionThrown() {
        try {
            Closeable closable = new Closeable() {
                @Override
                public void close() throws IOException {
                    throw new IOException();
                }
            };
            
            IOUtils.closeQuietly(closable);
            
            fail("IOException not thrown");
        } catch (IOException e) {
            assertTrue(true); // IOException should be thrown
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 2
    @Test
    public void testCloseQuietly_CleanClose() {
        try {
            Closeable closable = new Closeable() {
                private boolean closed = false;
                
                @Override
                public void close() throws IOException {
                    closed = true;
                }
                
                public boolean isClosed() {
                    return closed;
                }
            };
            
            IOUtils.closeQuietly(closable);
            
            assertTrue(closable.isClosed());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Expert 3
    @Test
    public void testConstructor() {
        try {
            IOUtils ioUtils = new IOUtils();
            assertNotNull(ioUtils);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}