package org.junit.runner;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

public class MockitoJUnitRunner extends Runner {
    @Override
    public void run(RunNotifier notifier) {
        JUnitCore core = new JUnitCore();
        Result result = core.run(MultiDirectionalTest.class);
        notifier.fireTestFinished(result);
    }
}