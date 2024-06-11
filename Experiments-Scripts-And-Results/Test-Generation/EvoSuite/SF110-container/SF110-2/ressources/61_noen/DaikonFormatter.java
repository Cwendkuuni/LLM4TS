package fi.vtt.noen.testgen.observations.formatter;

import fi.vtt.noen.testgen.parser.*;
import java.io.*;
import fi.vtt.noen.testgen.model.*;
import java.util.*;
import fi.vtt.noen.testgen.observations.data.*;

public class DaikonFormatter extends BasicFormatter
{
    private Set<String> declarations;
    private boolean simple;
    private final Collection<String> inputs;
    
    public DaikonFormatter(final String fileName, final boolean simple) throws IOException {
        super(fileName);
        this.declarations = new HashSet<String>();
        this.simple = false;
        this.simple = simple;
        if (simple) {
            this.inputs = null;
            return;
        }
        try {
            this.inputs = InterfaceParser.methodNames(inputs());
            System.out.println("inputs:" + this.inputs);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("failed to load input and output interface definitions for daikon formatter", e);
        }
    }
    
    public DaikonFormatter(final String fileName, final Collection<String> inputs) throws IOException {
        super(fileName);
        this.declarations = new HashSet<String>();
        this.simple = false;
        this.inputs = inputs;
        System.out.println("inputs:" + inputs);
    }
    
    private static Class classForProperty(final String property) throws Exception {
        final Properties configuration = new Properties();
        configuration.load(new FileInputStream("testgen.properties"));
        final String className = configuration.getProperty(property);
        if (className == null) {
            return null;
        }
        System.out.println("creating class for:" + className);
        return Class.forName(className);
    }
    
    private static Collection<Class> classesForMultipleProperties(final String prefix) throws Exception {
        final Collection<Class> classes = new ArrayList<Class>();
        int index = 1;
        while (true) {
            final Class clazz = classForProperty(prefix + index);
            ++index;
            if (clazz == null) {
                break;
            }
            classes.add(clazz);
        }
        return classes;
    }
    
    public static Collection<Class> inputs() throws Exception {
        return classesForMultipleProperties("InputInterface");
    }
    
    public static Collection<Class> outputs() throws Exception {
        return classesForMultipleProperties("OutputInterface");
    }
    
    @Override
    public String header() {
        final OutputBuffer out = new OutputBuffer();
        out.append("decl-version 2.0");
        out.append("var-comparability implicit");
        return out.toString();
    }
    
    @Override
    public String footer() {
        return "";
    }
    
    @Override
    public String fileNameExtension() {
        return "dtrace";
    }
    
    @Override
    public String observations(final ProgramRun run) {
        final OutputBuffer out = new OutputBuffer();
        Event previousEvent = null;
        Event event = Const.INITIAL_STATE;
        final Iterator<Event> ei = run.iterator();
        while (ei.hasNext()) {
            previousEvent = event;
            event = ei.next();
            if (this.simple) {
                this.simplePoint(event, "10", out);
            }
            else {
                if ((this.inputs.contains(previousEvent.getName()) && this.inputs.contains(event.getName())) || (!previousEvent.isExit() && event.isExit() && (previousEvent.getName() + "_EXIT").equals(event.getName()))) {
                    this.point(previousEvent.getName(), previousEvent, "11", out);
                }
                if (previousEvent.isExit()) {
                    this.point(previousEvent.getName(), previousEvent, "22", out);
                }
                else {
                    if (!this.inputs.contains(previousEvent.getName()) || this.inputs.contains(event.getName()) || previousEvent.isExit() || event.isExit()) {
                        continue;
                    }
                    final String name = previousEvent.getName() + "," + event.getName();
                    this.point(name, previousEvent, "22", out);
                }
            }
        }
        if (!this.simple && (this.inputs.contains(event.getName()) || event.isExit())) {
            this.point(event.getName(), event, "11", out);
        }
        return out.toString();
    }
    
    private void simplePoint(final Event event, final String comparability, final OutputBuffer out) {
        String name = event.getName();
        name = name.replace(' ', '_');
        if (!event.isExit()) {
            name += ":::ENTER";
        }
        else {
            name += ":::EXIT1";
        }
        this.declarations(event, name, out, comparability);
        out.append("");
        out.append(name);
        this.attributes(event, out);
    }
    
    private void point(String name, final Event event, final String comparability, final OutputBuffer out) {
        name = name.replace(' ', '_');
        name += ":::ENTER";
        this.declarations(event, name, out, comparability);
        out.append("");
        out.append(name);
        this.attributes(event, out);
    }
    
    private void declarations(final Event event, final String eventName, final OutputBuffer out, final String comparability) {
        if (this.declarations.contains(eventName)) {
            return;
        }
        this.declarations.add(eventName);
        out.append("");
        out.append("ppt " + eventName);
        if (event.isExit()) {
            out.append("ppt-type exit");
        }
        else {
            out.append("ppt-type enter");
        }
        final Iterator<EventAttribute> ai = event.attributes();
        while (ai.hasNext()) {
            final EventAttribute attribute = ai.next();
            if (attribute instanceof ArrayAttribute) {
                out.append("  variable " + attribute.getName() + "[]");
                out.append("    var-kind array");
            }
            else {
                out.append("  variable " + attribute.getName());
                out.append("    var-kind variable");
            }
            out.append("    dec-type " + attribute.getType());
            out.append("    rep-type " + attribute.getType());
            out.append("    comparability " + comparability);
        }
    }
    
    private void attributes(final Event event, final OutputBuffer out) {
        final Iterator<EventAttribute> ai = event.attributes();
        while (ai.hasNext()) {
            final EventAttribute attribute = ai.next();
            if (attribute instanceof ArrayAttribute) {
                out.append(attribute.getName() + "[]");
            }
            else {
                out.append(attribute.getName());
            }
            if (attribute.getType().equals("java.lang.String")) {
                out.append('\"' + attribute.getValue() + '\"');
            }
            else {
                out.append(attribute.getValue());
            }
            out.append("1");
        }
    }
}
