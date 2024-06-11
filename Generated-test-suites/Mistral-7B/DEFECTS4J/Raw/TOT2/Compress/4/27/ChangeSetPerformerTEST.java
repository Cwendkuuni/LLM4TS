// ChangeSetPerformerTest.java

package org.apache.commons.compress.changes;

import org.junit.Test;
import static org.junit.Assert.*;

public class ChangeSetPerformerTest {

    @Test
    public void testChangeSetPerformer() {
        // Test the class with the given test cases from each expert
        ChangeSetPerformerTest1 test1 = new ChangeSetPerformerTest1();
        ChangeSetPerformerTest2 test2 = new ChangeSetPerformerTest2();
        ChangeSetPerformerTest3 test3 = new ChangeSetPerformerTest3();

        // Run all tests
        test1.runTests();
        test2.runTests();
        test3.runTests();
    }
}

class ChangeSetPerformerTest1 {
    private void runTests() {
        testPerform();
    }

    @Test
    public void testPerform() {
        // Test code from Expert 1
    }
}

class ChangeSetPerformerTest2 {
    private void runTests() {
        testPerform();
    }

    @Test
    public void testPerform() {
        // Test code from Expert 2
    }
}

class ChangeSetPerformerTest3 {
    private void runTests() {
        testPerform();
    }

    @Test
    public void testPerform() {
        // Test code from Expert 3
    }
}