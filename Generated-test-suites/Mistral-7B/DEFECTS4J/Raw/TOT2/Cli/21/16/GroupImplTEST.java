import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.TestUtils;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupWithValidOptions() {
        // Given
        final List options = new ArrayList();
        options.add(Option.builder("option1")
                .longOpt("long-option1")
                .required(false)
                .build());
        options.add(Option.builder("option2")
                .longOpt("long-option2")
                .required(false)
                .build());

        Group group = new GroupImpl(options, "MyGroup", null, 1, 1, false);

        // When
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        group.process(commandLine, commandLine.getArguments().listIterator());

        // Then
        TestUtils.assertCommandLineParsed(commandLine, group.getPreferredName(), options.toArray(new Option[0]));
    }

    @Test
    public void testGroupWithRequiredOption() {
        // Given
        final List options = new ArrayList();
        options.add(Option.builder("option1")
                .longOpt("long-option1")
                .required(true)
                .build());

        Group group = new GroupImpl(options, "MyGroup", null, 1, 1, false);

        // When
        WriteableCommandLine commandLine = TestUtils.createCommandLine();

        // Then
        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.getArguments().listIterator()));
    }

    @Test
    public void testGroupWithMinimumOption() {
        // Given
        final List options = new ArrayList();
        options.add(Option.builder("option1")
                .longOpt("long-option1")
                .required(false)
                .build());
        options.add(Option.builder("option2")
                .longOpt("long-option2")
                .required(false)
                .build());
        options.add(Option.builder("option3")
                .longOpt("long-option3")
                .required(false)
                .build());

        Group group = new GroupImpl(options, "MyGroup", null, 1, 3, false);

        // When
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        commandLine.addOption("option1");
        commandLine.addOption("option2");
        group.process(commandLine, commandLine.getArguments().listIterator());

        // Then
        TestUtils.assertCommandLineParsed(commandLine, group.getPreferredName(), options.toArray(new Option[0]));
    }

    @Test
    public void testGroupWithMaximumOption() {
        // Given
        final List options = new ArrayList();
        options.add(Option.builder("option1")
                .longOpt("long-option1")
                .required(false)
                .build());
        options.add(Option.builder("option2")
                .longOpt("long-option2")
                .required(false)
                .build());
        options.add(Option.builder("option3")
                .longOpt("long-option3")
                .required(false)
                .build());

        Group group = new GroupImpl(options, "MyGroup", null, 1, 2, false);

        // When
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        commandLine.addOption("option1");
        commandLine.addOption("option2");
        commandLine.addOption("option3");

        // Then
        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.getArguments().listIterator()));
    }
}