import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;


public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;

    @Before
    public void setUp() {
        ChangeSet changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testConstructorCreation() {
        assertNotNull(performer);
    }

    @Test
    public void testPerform() throws IOException {
        ArchiveInputStream in = createTestArchiveInputStream();
        ArchiveOutputStream out = createTestArchiveOutputStream();

        ChangeSetResults results = performer.perform(in, out);

        assertNotNull(results);
    }

    private ArchiveInputStream createTestArchiveInputStream() {
        return new ArchiveInputStream() {
            @Override
            public ArchiveEntry getNextEntry() throws IOException {
                return null;
            }

            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }

    private ArchiveOutputStream createTestArchiveOutputStream() {
        return new ArchiveOutputStream() {
            @Override
            public void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException {

            }

            @Override
            public void closeArchiveEntry() throws IOException {

            }

            @Override
            public void flush() throws IOException {

            }

            @Override
            public void write(byte[] bytes, int i, int i1) throws IOException {

            }

            @Override
            public ArchiveEntry createArchiveEntry(String s, long l) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testIsDeletedLater() {
        Set workingSet = new LinkedHashSet<>();
        ArchiveEntry entry = createTestArchiveEntry("test_entry");
        boolean deletedLater = performer.isDeletedLater(workingSet, entry);
        assertFalse(deletedLater);
    }

    private ArchiveEntry createTestArchiveEntry(String name) {
        return new ArchiveEntry() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void setName(String s) {

            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public void setSize(long l) {

            }

            @Override
            public boolean isDirectory() {
                return false;
            }

            @Override
            public void setDirectory(boolean b) {

            }

            @Override
            public void setMode(int i) {

            }

            @Override
            public int getMode() {
                return 0;
            }

            @Override
            public void setUserId(int i) {

            }

            @Override
            public int getUserId() {
                return 0;
            }

            @Override
            public void setGroupId(int i) {

            }

            @Override
            public int getGroupId() {
                return 0;
            }

            @Override
            public int getDevMajor() {
                return 0;
            }

            @Override
            public void setDevMajor(int i) {

            }

            @Override
            public int getDevMinor() {
                return 0;
            }

            @Override
            public void setDevMinor(int i) {

            }

            @Override
            public int getLinkFlag() {
                return 0;
            }

            @Override
            public void setLinkFlag(int i) {

            }

            @Override
            public boolean isLink() {
                return false;
            }

            @Override
            public void setLink(boolean b) {

            }

            @Override
            public String getLinkName() {
                return null;
            }

            @Override
            public void setLinkName(String s) {

            }

            @Override
            public int getUserId(int i) {
                return 0;
            }

            @Override
            public int getGroupId(int i) {
                return 0;
            }

            @Override
            public void setIds(int i, int i1) {

            }

            @Override
            public boolean isStar() {
                return false;
            }

            @Override
            public void setStar(boolean b) {

            }

            @Override
            public void setMagicMatcher(MagicMatcher magicMatcher) {

            }

            @Override
            public boolean matches(MagicMatcher magicMatcher) throws IOException {
                return false;
            }
        };
    }

    @Test
    public void testCopyStream() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream out = createTestArchiveOutputStream();
        ArchiveEntry entry = createTestArchiveEntry("test_entry");

        assertDoesNotThrow(() -> {
            performer.copyStream(in, out, entry);
        });
    }

}