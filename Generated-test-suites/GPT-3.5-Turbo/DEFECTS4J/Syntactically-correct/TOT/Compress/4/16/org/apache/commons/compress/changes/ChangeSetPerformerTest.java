import org.junit.Test;

import static org.junit.Assert.*;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform_AddAndReplace() {
        // Arrange
        ChangeSet changeSet = new ChangeSet();
        Change change1 = new Change(Change.TYPE_ADD);
        change1.setReplaceMode(true);
        Change change2 = new Change(Change.TYPE_ADD);
        change2.setReplaceMode(true);
        Change change3 = new Change(Change.TYPE_DELETE);
        change3.setTargetFile("file1.txt");
        changeSet.addChange(change1);
        changeSet.addChange(change2);
        changeSet.addChange(change3);

        // Act
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);

        // Assert
        assertTrue(changeSet.getChanges().isEmpty());
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
    }

    @Test
    public void testPerform_EntryIsDeletedLater() {
        // Arrange
        ChangeSet changeSet = new ChangeSet();
        Change change1 = new Change(Change.TYPE_ADD);
        change1.setReplaceMode(true);
        Change change2 = new Change(Change.TYPE_DELETE);
        change2.setTargetFile("file1.txt");
        changeSet.addChange(change1);
        changeSet.addChange(change2);

        // Act
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);

        // Assert
        assertTrue(changeSet.getChanges().isEmpty());
        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getDeleted().size());
    }

    @Test
    public void testPerform_EntryNotDeletedLater() {
        // Arrange
        ChangeSet changeSet = new ChangeSet();
        Change change1 = new Change(Change.TYPE_DELETE);
        change1.setTargetFile("file1.txt");
        changeSet.addChange(change1);

        // Act
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);

        // Assert
        assertTrue(changeSet.getChanges().isEmpty());
        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
    }

    @Test
    public void testPerform_AddNotReplace() {
        // Arrange
        ChangeSet changeSet = new ChangeSet();
        Change change1 = new Change(Change.TYPE_ADD);
        change1.setReplaceMode(false);
        changeSet.addChange(change1);

        // Act
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);

        // Assert
        assertTrue(changeSet.getChanges().isEmpty());
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getDeleted().size());
    }
}