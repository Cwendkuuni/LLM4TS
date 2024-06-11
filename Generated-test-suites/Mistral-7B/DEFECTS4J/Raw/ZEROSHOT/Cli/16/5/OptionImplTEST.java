import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.base.Option;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        Option option = new OptionImpl(1, true);
        Assert.assertEquals(1, option.getId());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcess() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator<String> arguments = mock(ListIterator.class);

        when(commandLine.getArguments()).thenReturn(arguments);

        when(options -> options.findOption("test"))
                .thenReturn(option);

        Options options = new Options();

        Assert.assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcessWithInvalidArgument() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator<String> arguments = mock(ListIterator.class);

        when(commandLine.getArguments()).thenReturn(arguments);

        when(options -> options.findOption("test"))
                .thenReturn(option);

        options = new Options();

        Assert.assertFalse(option.canProcess(commandLine, arguments));

        when(arguments.next()).thenReturn("invalid");
        Assert.assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        Option option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test";
            }

            @Override
            public String getDescription() {
                return "Test option";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return new Iterable<String>() {
                    @Override
                    public Iterator<String> iterator() {
                        return new Iterator<String>() {
                            @Override
                            public boolean hasNext() {
                                return true;
                            }

                            @Override
                            public String next() {
                                return "test";
                            }
                        };
                    }
                };
            }

            @Override
            public Iterable<String> getTriggers() {
                return new Iterable<String>() {
                    @Override
                    public Iterator<String> iterator() {
                        return new Iterator<String>() {
                            @Override
                            public boolean hasNext() {
                                return true;
                            }

                            @Override
                            public String next() {
                                return "test";
                            }
                        };
                    }
                };
            }
        };

        Assert.assertEquals("Test option\n" +
                "  -test TEST\n", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test";
            }
        };

        Option option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test";
            }
        };

        Option option3 = new OptionImpl(2, true) {
            @Override
            public String getPreferredName() {
                return "test";
            }
        };

        Assert.assertTrue(option1.equals(option1));
        Assert.assertTrue(option1.equals(option2));
        Assert.assertFalse(option1.equals(option3));
    }

    @Test
    public void testHashCode() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test";
            }
        };

        Option option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "test";
            }
        };

        Option option3 = new OptionImpl(2, true) {
            @Override
            public String getPreferredName() {
                return "test";
            }
        };

        Assert.assertEquals(option1.hashCode(), option1.hashCode());
        Assert.assertEquals(option1.hashCode(), option2.hashCode());
        Assert.assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testFindOption() {
        Option option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getTriggers() {
                return new Iterable<String>() {
                    @Override
                    public Iterator<String> iterator() {
                        return new Iterator<String>() {
                            @Override
                            public boolean hasNext() {
                                return true;
                            }

                            @Override
                            public String next() {
                                return "test";
                            }
                        };
                    }
                };
            }
        };

        Options options = new Options();
        options.addOption(option);

        Option foundOption = option.findOption("test");
        Assert.assertEquals(option, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new OptionImpl(1, true);
        Assert.assertTrue(option.isRequired());

        Option option2 = new OptionImpl(1, false);
        Assert.assertFalse(option2.isRequired());
    }

    @Test
    public void testDefaults() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        when(option.defaults(commandLine))
                .thenReturn(commandLine);

        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public Option defaults(WriteableCommandLine commandLine) {
                option.defaults(commandLine);
                return option;
            }
        };

        CommandLine commandLine = mock(CommandLine.class);
        when(commandLine.getArgs()).thenReturn(new String[0]);

        optionImpl.defaults(commandLine);

        Assert.assertEquals("", commandLine.getOptionValue("test"));
    }

    @Test
    public void testCheckPrefixes() {
        Option option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getTriggers() {
                return new Iterable<String>() {
                    @Override
                    public Iterator<String> iterator() {
                        return new Iterator<String>() {
                            @Override
                            public boolean hasNext() {
                                return true;
                            }

                            @Override
                            public String next() {
                                return "test";
                            }
                        };
                    }
                };
            }

            @Override
            public Set<String> getPrefixes() {
                return null;
            }
        };

        Set<String> prefixes = mock(Set.class);

        try {
            option.checkPrefixes(prefixes);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("test needs a prefix in []", e.getMessage());
        }
    }

    @Test
    public void testCheckPrefixesWithPrefixes() {
        Option option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getTriggers() {
                return new Iterable<String>() {
                    @Override
                    public Iterator<String> iterator() {
                        return new Iterator<String>() {
                            @Override
                            public boolean hasNext() {
                                return true;
                            }

                            @Override
                            public String next() {
                                return "test";
                            }
                        };
                    }
                };
            }

            @Override
            public Set<String> getPrefixes() {
                return mock(Set.class);
            }
        };

        option.checkPrefixes(null);
    }
}