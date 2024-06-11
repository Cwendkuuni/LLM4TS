package com.ib.client;

public class ComboLeg
{
    public static final int SAME = 0;
    public static final int OPEN = 1;
    public static final int CLOSE = 2;
    public static final int UNKNOWN = 3;
    public int m_conId;
    public int m_ratio;
    public String m_action;
    public String m_exchange;
    public int m_openClose;
    public int m_shortSaleSlot;
    public String m_designatedLocation;
    
    public ComboLeg() {
        this(0, 0, null, null, 0, 0, null);
    }
    
    public ComboLeg(final int p_conId, final int p_ratio, final String p_action, final String p_exchange, final int p_openClose) {
        this(p_conId, p_ratio, p_action, p_exchange, p_openClose, 0, null);
    }
    
    public ComboLeg(final int p_conId, final int p_ratio, final String p_action, final String p_exchange, final int p_openClose, final int p_shortSaleSlot, final String p_designatedLocation) {
        this.m_conId = p_conId;
        this.m_ratio = p_ratio;
        this.m_action = p_action;
        this.m_exchange = p_exchange;
        this.m_openClose = p_openClose;
        this.m_shortSaleSlot = p_shortSaleSlot;
        this.m_designatedLocation = p_designatedLocation;
    }
    
    @Override
    public boolean equals(final Object p_other) {
        if (this == p_other) {
            return true;
        }
        if (p_other == null) {
            return false;
        }
        final ComboLeg l_theOther = (ComboLeg)p_other;
        return this.m_conId == l_theOther.m_conId && this.m_ratio == l_theOther.m_ratio && this.m_openClose == l_theOther.m_openClose && this.m_shortSaleSlot == l_theOther.m_shortSaleSlot && Util.StringCompareIgnCase(this.m_action, l_theOther.m_action) == 0 && Util.StringCompareIgnCase(this.m_exchange, l_theOther.m_exchange) == 0 && Util.StringCompareIgnCase(this.m_designatedLocation, l_theOther.m_designatedLocation) == 0;
    }
}
