package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import java.util.*;

public class ArgsParser
{
    public static final String SHORT_ARGUMENT_INDICATOR = "-";
    public static final String LONG_ARGUMENT_INDICATOR = "--";
    private String[] args;
    private String[] innerArgs;
    
    public ArgsParser() {
    }
    
    public ArgsParser(final String[] args) {
        this.setArgs(args);
    }
    
    public int getArgsCount() {
        return this.args.length;
    }
    
    public String[] getArgs() {
        return this.args;
    }
    
    public void setArgs(final String[] args) {
        if (args == null) {
            throw new NullPointerException("args[] cannot be null.");
        }
        this.args = new String[args.length];
        this.innerArgs = new String[args.length];
        for (int i = 0; i < args.length; ++i) {
            this.args[i] = args[i];
            this.innerArgs[i] = args[i];
        }
    }
    
    public int getArgsLeftCount() {
        int result = 0;
        for (int i = 0; i < this.innerArgs.length; ++i) {
            if (this.innerArgs[i] != null) {
                ++result;
            }
        }
        return result;
    }
    
    public SwitchArgument parseSwitchArgument(final String key) {
        final boolean isLongKey = key.length() > 1;
        if (isLongKey) {
            final String searchFor = "--" + key;
            for (int i = 0; i < this.args.length; ++i) {
                if (this.innerArgs[i] != null && this.innerArgs[i].equals(searchFor)) {
                    this.innerArgs[i] = null;
                    return new SwitchArgument(i, key, true);
                }
            }
        }
        else {
            for (int i = 0; i < this.args.length; ++i) {
                if (this.innerArgs[i] != null && this.innerArgs[i].length() > 1 && this.innerArgs[i].startsWith("-") && !this.innerArgs[i].startsWith("--")) {
                    final int index = this.innerArgs[i].lastIndexOf(key);
                    if (index > 0) {
                        final String before = this.innerArgs[i].substring(0, index);
                        final String after = this.innerArgs[i].substring(index + 1, this.innerArgs[i].length());
                        this.innerArgs[i] = before + after;
                        if (this.innerArgs[i].replace("-", " ").trim().length() == 0) {
                            this.innerArgs[i] = null;
                        }
                        return new SwitchArgument(i, key, true);
                    }
                }
            }
        }
        return new SwitchArgument(-1, key, false);
    }
    
    public ParameterArgument parseParameterArgument(final String key) {
        final boolean isLongKey = key.length() > 1;
        if (isLongKey) {
            final String searchFor = "--" + key;
            int i = 0;
            while (i < this.innerArgs.length) {
                if (this.innerArgs[i] != null && this.innerArgs[i].equals(searchFor)) {
                    if (this.innerArgs.length > i + 1 && this.innerArgs[i] != null && this.innerArgs[i + 1] != null) {
                        final String value = this.innerArgs[i + 1];
                        this.innerArgs[i] = null;
                        this.innerArgs[i + 1] = null;
                        return new ParameterArgument(i, key, value);
                    }
                    return null;
                }
                else {
                    ++i;
                }
            }
        }
        else {
            for (int i = 0; i < this.args.length; ++i) {
                if (this.innerArgs[i] != null && this.innerArgs[i].length() > 1 && this.innerArgs[i].startsWith("-") && !this.innerArgs[i].startsWith("--")) {
                    final int index = this.innerArgs[i].indexOf(key);
                    if (index > 0) {
                        if (index + 1 != this.innerArgs[i].length()) {
                            final String before = this.innerArgs[i].substring(0, index);
                            final String value2 = this.innerArgs[i].substring(index + 1, this.innerArgs[i].length());
                            this.innerArgs[i] = before;
                            if (this.innerArgs[i].replace("-", " ").trim().length() == 0) {
                                this.innerArgs[i] = null;
                            }
                            return new ParameterArgument(i, key, value2);
                        }
                        if (this.innerArgs.length > i + 1 && this.innerArgs[i] != null && this.innerArgs[i + 1] != null) {
                            final String before = this.innerArgs[i].substring(0, index);
                            final String after = this.innerArgs[i].substring(index + 1, this.innerArgs[i].length());
                            this.innerArgs[i] = before + after;
                            if (this.innerArgs[i].replace("-", " ").trim().length() == 0) {
                                this.innerArgs[i] = null;
                            }
                            final String value3 = this.innerArgs[i + 1];
                            this.innerArgs[i + 1] = null;
                            return new ParameterArgument(i, key, value3);
                        }
                        return null;
                    }
                }
            }
        }
        return null;
    }
    
    public List<StringArgument> parseStringArgument() {
        final List<StringArgument> result = new ArrayList<StringArgument>();
        for (int i = 0; i < this.args.length; ++i) {
            if (this.innerArgs[i] != null) {
                result.add(new StringArgument(i, this.innerArgs[i]));
                this.innerArgs[i] = null;
            }
        }
        return result;
    }
}