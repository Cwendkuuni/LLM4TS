package com.lts.swing.combobox;

import javax.swing.*;

public class SimpleComboBox extends JComboBox
{
    private static final long serialVersionUID = 1L;
    private SimpleComboBoxModel mySimpleModel;

    public SimpleComboBox(final SimpleComboBoxModel model) {
        super(model);
    }

    @Override
    public void setModel(final ComboBoxModel model) {
        if (model instanceof SimpleComboBoxModel) {
            this.mySimpleModel = (SimpleComboBoxModel)model;
        }
        super.setModel(model);
    }

    public Object getSelectedValue() {
        if (null == this.mySimpleModel) {
            return null;
        }
        return this.mySimpleModel.getSelectedValue();
    }

    public void setSelectedValue(final Object value) {
        this.mySimpleModel.setSelectedValue(value);
    }

    public int getSelectedInt() {
        final Object o = this.getSelectedValue();
        if (null == o) {
            return -1;
        }
        if (o instanceof Integer) {
            final Integer ival = (Integer)o;
            return ival;
        }
        if (o instanceof Long) {
            final Long lval = (Long)o;
            return (int)(Object)lval;
        }
        if (o instanceof Short) {
            final Short sval = (Short)o;
            return sval;
        }
        if (o instanceof Byte) {
            final Byte bval = (Byte)o;
            return bval;
        }
        throw new IllegalArgumentException();
    }

    public long getSelectedLong() {
        final Object o = this.getSelectedValue();
        if (null == o) {
            return -1L;
        }
        if (o instanceof Integer) {
            final Integer ival = (Integer)o;
            return ival;
        }
        if (o instanceof Long) {
            final Long lval = (Long)o;
            return lval;
        }
        if (o instanceof Short) {
            final Short sval = (Short)o;
            return sval;
        }
        if (o instanceof Byte) {
            final Byte bval = (Byte)o;
            return bval;
        }
        throw new IllegalArgumentException();
    }
}