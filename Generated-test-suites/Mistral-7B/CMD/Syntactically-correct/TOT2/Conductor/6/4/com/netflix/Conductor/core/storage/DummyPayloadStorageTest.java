package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

@RunWith(Parameterized.class)
@Category(IntegrationTest.class)
public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;

@Parameterized.Parameters(name = "{index}-{0}")
public static Iterable<Object[]> data() {
return new Object[][] {
// Add any data if required
};
}

@Parameterized.Constructor
public DummyPayloadStorageTest(DummyPayloadStorage dummyPayloadStorage) {
this.dummyPayloadStorage = dummyPayloadStorage;
}

@Test
public void testGetLocation() {
Operation operation = new Operation();
PayloadType payloadType = PayloadType.JSON;
String path = "test-path";

ExternalStorageLocation expectedResult = null;
ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

assertNull(result);
assertEquals(expectedResult, result);
}

// Add getLocation() test cases with edge cases and exceptions as shown above


@Test
public void testUpload() {
    String path = "test-path";
    InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
    long payloadSize = 10L;

    dummyPayloadStorage.upload(path, inputStream, payloadSize);

    // Verification can be added here if required
}

// Add upload() test cases with edge cases and exceptions as shown above

@Test
public void testDownload() {
    String path = "test-path";

    InputStream expectedResult = new ByteArrayInputStream("test data".getBytes());
    InputStream result = dummyPayloadStorage.download(path);

    assertNull(result);
    assertEquals(expectedResult, result);
}

// Add download() test cases with edge cases and exceptions as shown above

@Test
public void testUploadAndDownload() throws IOException {
    String path = "test-path";
    InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
    long payloadSize = 10L;

    dummyPayloadStorage.upload(path, inputStream, payloadSize);
    InputStream downloadedData = dummyPayloadStorage.download(path);

    assertNotNull(downloadedData);
    assertEquals("test data", downloadedData.readAllBytes());
}

@Test
public void testGetLocationWithValidParameters() {
    Operation operation = new Operation();
    PayloadType payloadType = PayloadType.JSON;
    String path = "test-path";
    ExternalStorageLocation expectedResult = new ExternalStorageLocation("test-location", "test-url");
    ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

    assertNotNull(result);
    assertSame(expectedResult, result);
}
}