package org.apache.commons.compress.changes.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.apache.commons.compress.changes.ChangeSetPerformer;

public class ChangeSetPerformerTest {
private ChangeSetPerformer performer;
private ArchiveInputStream input;
private ArchiveOutputStream output;
private ChangeSet changeSet;
private Set<Change> changes;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
changeSet = new ChangeSet();
changes = changeSet.getChanges();
performer = new ChangeSetPerformer(changeSet);
input = mock(ArchiveInputStream.class);
output = new MemoryArchiveOutputStream();
}

// Test cases for constructor method, perform method with valid input, empty ChangeSet, conflicting changes, null ChangeSet,
// null input or output streams, invalid entry name, duplicate changes, empty input stream, and mixed input and change set entries
// (Expert 1, 2, and 3's test cases)

// Additional test cases for edge cases and exceptions

@Test
public void testPerformWithEmptyInputChangeSet() throws IOException {
Change change = new Change("src/test/resources/testfile.txt", Change.TYPE_ADD, null, null);
changes.add(change);
IOUtils.copy(getClass().getResourceAsStream("/empty_input_archive.zip"), input);

ChangeSetResults results = performer.perform(input, output);
assertEquals(0, results.getNumAddedFromChangeSet());
assertEquals(0, results.getNumAddedFromStream());
assertEquals(0, results.getNumDeleted());
}

@Test
public void testPerformWithMissingInputEntry() throws IOException {
Change change = new Change("src/test/resources/testfile.txt", Change.TYPE_DELETE, null, null);
changes.add(change);
IOUtils.copy(getClass().getResourceAsStream("/input_archive_with_missing_entry.zip"), input);

ChangeSetResults results = performer.perform(input, output);
assertEquals(0, results.getNumAddedFromChangeSet());
assertEquals(0, results.getNumAddedFromStream());
assertEquals(1, results.getNumDeleted());
}

@Test
public void testPerformWithInvalidChangeType() throws IOException {
Change change = new Change("src/test/resources/testfile.txt", 5, null, null);
changes.add(change);
IOUtils.copy(getClass().getResourceAsStream("/input_archive.zip"), input);

try {
performer.perform(input, output);
fail("Expected an IllegalArgumentException");
} catch (IllegalArgumentException e) {
// Expected exception
}
}

@Test
public void testPerformWithMissingChangeTarget() throws IOException {
Change change = new Change(null, Change.TYPE_ADD, null, null);
changes.add(change);
IOUtils.copy(getClass().getResourceAsStream("/input_archive.zip"), input);

try {
performer.perform(input, output);
fail("Expected an IllegalArgumentException");
} catch (IllegalArgumentException e) {
// Expected exception
}
}

@Test
public void testPerformWithEmptyChangeTarget() throws IOException {
Change change = new Change("", Change.TYPE_ADD, null, null);
changes.add(change);
IOUtils.copy(getClass().getResourceAsStream("/input_archive.zip"), input);

try {
performer.perform(input, output);
fail("Expected an IllegalArgumentException");
} catch (IllegalArgumentException e) {
// Expected exception
}
}

@Test
public void testPerformWithOverlappingChanges() throws IOException {
Change change1 = new Change("src/test/resources/testfile.txt", Change.TYPE_ADD, null, null);
Change change2 = new Change("src/test/resources/testfile.txt", Change.TYPE_ADD, null, null);
changes.add(change1);
changes.add(change2);
IOUtils.copy(getClass().getResourceAsStream("/input_archive.zip"), input);

try {
performer.perform(input, output);
fail("Expected an IllegalStateException");
} catch (IllegalStateException e) {
// Expected exception
}
}

@Test
public void testPerformWithConflictingChanges() throws IOException {
Change change1 = new Change("src/test/resources/testfile.txt", Change.TYPE_DELETE, null, null);
Change change2 = new Change("src/test/resources/testfile.txt", Change.TYPE_ADD, null, null);
changes.add(change1);
changes.add(change2);
IOUtils.copy(getClass().getResourceAsStream("/input_archive.zip"), input);

try {
performer.perform(input, output);
fail("Expected an IllegalStateException");
} catch (IllegalStateException e) {
// Expected exception
}
}

@Test
public void testPerformWithInvalidChangeTargetDirectory() throws IOException {
Change change = new Change("/test/testfile.txt", Change.TYPE_ADD, null, null);
changes.add(change);
IOUtils.copy(getClass().getResourceAsStream("/input_archive.zip"), input);

try {
performer.perform(input, output);
fail("Expected an IllegalArgumentException");
} catch (IllegalArgumentException e) {
// Expected exception
}
}

@Test
public void testPerformWithInvalidArchiveType() throws IOException {
ArchiveInputStream input = new MockArchiveInputStream();
ArchiveOutputStream output = new MemoryArchiveOutputStream();
ChangeSet changeSet = new ChangeSet();
ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

try {
performer.perform(input, output);
fail("Expected an UnsupportedOperationException");
} catch (UnsupportedOperationException e) {
// Expected exception
}
}

@After
public void cleanup() throws IOException {
output.finish();
IOUtils.closeQuietly(input);
IOUtils.closeQuietly(output);
}
}