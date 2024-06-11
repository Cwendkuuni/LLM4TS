package org.znerd.xmlenc;

public final class XMLChecker
{
    public static final void checkS(final String s) throws NullPointerException {
        checkS(s.toCharArray(), 0, s.length());
    }
    
    public static final void checkS(final char[] ch, final int start, final int length) throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException {
        for (int i = start; i < length; ++i) {
            final int c = ch[i];
            if (c != 32 && c != 9 && c != 13 && c != 10) {
                throw new InvalidXMLException("The character 0x" + Integer.toHexString(c) + " is not valid for the 'S' production (white space).");
            }
        }
    }
    
    public static final boolean isName(final String s) throws NullPointerException {
        try {
            checkName(s);
            return true;
        }
        catch (InvalidXMLException exception) {
            return false;
        }
    }
    
    public static final void checkName(final String s) throws NullPointerException, InvalidXMLException {
        checkName(s.toCharArray(), 0, s.length());
    }
    
    public static final void checkName(final char[] ch, final int start, final int length) throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException {
        if (length < 1) {
            throw new InvalidXMLException("An empty string does not match the 'Name' production.");
        }
        int i = start;
        char c = ch[i];
        if (c != '_' && c != ':' && !isLetter(c)) {
            throw new InvalidXMLException("The character 0x" + Integer.toHexString(c) + " is invalid as a starting character in the 'Name' production.");
        }
        ++i;
        while (i < length) {
            c = ch[i];
            if (!isNameChar(c)) {
                throw new InvalidXMLException("The character 0x" + Integer.toHexString(c) + " is not valid for the 'Name' production.");
            }
            ++i;
        }
    }
    
    public static final boolean isSystemLiteral(final String s) throws NullPointerException {
        try {
            checkSystemLiteral(s);
            return true;
        }
        catch (InvalidXMLException exception) {
            return false;
        }
    }
    
    public static final void checkSystemLiteral(final String s) throws NullPointerException, InvalidXMLException {
        checkSystemLiteral(s.toCharArray(), 0, s.length());
    }
    
    public static final void checkSystemLiteral(final char[] ch, final int start, final int length) throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException {
        if (length < 3) {
            throw new InvalidXMLException("Minimum length for the 'SystemLiteral' production is 3 characters.");
        }
        final int lastIndex = start + length - 1;
        final char firstChar = ch[0];
        final char lastChar = ch[lastIndex];
        if (firstChar == '\'') {
            if (lastChar != '\'') {
                throw new InvalidXMLException("First character is '\\'', but the last character is 0x" + Integer.toHexString(lastChar) + '.');
            }
            final String otherAllowedChars = "-()+,./:=?;!*#@$_%";
        }
        else {
            if (firstChar != '\"') {
                throw new InvalidXMLException("First char must either be '\\'' or '\"' instead of 0x" + Integer.toHexString(firstChar) + '.');
            }
            if (lastChar != '\"') {
                throw new InvalidXMLException("First character is '\"', but the last character is 0x" + Integer.toHexString(lastChar) + '.');
            }
            final String otherAllowedChars = "-'()+,./:=?;!*#@$_%";
        }
        int i = 1;
        while (i < length - 1) {
            final char c = ch[i];
            if (c == firstChar) {
                if (firstChar == '\'') {
                    throw new InvalidXMLException("Found '\\'' at position " + i + '.');
                }
                throw new InvalidXMLException("Found '\"' at position " + i + '.');
            }
            else {
                ++i;
            }
        }
    }
    
    public static final boolean isPubidLiteral(final String s) throws NullPointerException {
        try {
            checkPubidLiteral(s);
            return true;
        }
        catch (InvalidXMLException exception) {
            return false;
        }
    }
    
    public static final void checkPubidLiteral(final String s) throws NullPointerException, InvalidXMLException {
        checkPubidLiteral(s.toCharArray(), 0, s.length());
    }
    
    public static final void checkPubidLiteral(final char[] ch, final int start, final int length) throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException {
        if (length < 3) {
            throw new InvalidXMLException("Minimum length for the 'PubidLiteral' production is 3 characters.");
        }
        final int lastIndex = start + length - 1;
        final char firstChar = ch[0];
        final char lastChar = ch[lastIndex];
        String otherAllowedChars;
        if (firstChar == '\'') {
            if (lastChar != '\'') {
                throw new InvalidXMLException("First character is '\\'', but the last character is 0x" + Integer.toHexString(lastChar) + '.');
            }
            otherAllowedChars = "-()+,./:=?;!*#@$_%";
        }
        else {
            if (firstChar != '\"') {
                throw new InvalidXMLException("First char must either be '\\'' or '\"' instead of 0x" + Integer.toHexString(firstChar) + '.');
            }
            if (lastChar != '\"') {
                throw new InvalidXMLException("First character is '\"', but the last character is 0x" + Integer.toHexString(lastChar) + '.');
            }
            otherAllowedChars = "-'()+,./:=?;!*#@$_%";
        }
        for (int i = 1; i < length - 1; ++i) {
            final char c = ch[i];
            if (c != ' ' && c != '\r' && c != '\n' && !isLetter(c) && !isDigit(c) && otherAllowedChars.indexOf(c) < 0) {
                throw new InvalidXMLException("The character '" + c + "' (0x" + Integer.toHexString(c) + ") is not valid for the 'PubidLiteral' production.");
            }
        }
    }
    
    private static final boolean isNameChar(final char c) {
        return c == '.' || c == '-' || c == '_' || c == ':' || isDigit(c) || isLetter(c) || isCombiningChar(c) || isExtender(c);
    }
    
    private static final boolean isLetter(final char c) {
        return isBaseChar(c) || isIdeographic(c);
    }
    
    private static final boolean isBaseChar(final char c) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_1        /* n */
        //     2: iload_1         /* n */
        //     3: bipush          65
        //     5: if_icmplt       14
        //     8: iload_1         /* n */
        //     9: bipush          90
        //    11: if_icmple       2460
        //    14: iload_1         /* n */
        //    15: bipush          97
        //    17: if_icmplt       26
        //    20: iload_1         /* n */
        //    21: bipush          122
        //    23: if_icmple       2460
        //    26: iload_1         /* n */
        //    27: sipush          192
        //    30: if_icmplt       40
        //    33: iload_1         /* n */
        //    34: sipush          214
        //    37: if_icmple       2460
        //    40: iload_1         /* n */
        //    41: sipush          216
        //    44: if_icmplt       54
        //    47: iload_1         /* n */
        //    48: sipush          246
        //    51: if_icmple       2460
        //    54: iload_1         /* n */
        //    55: sipush          248
        //    58: if_icmplt       68
        //    61: iload_1         /* n */
        //    62: sipush          255
        //    65: if_icmple       2460
        //    68: iload_1         /* n */
        //    69: sipush          256
        //    72: if_icmplt       82
        //    75: iload_1         /* n */
        //    76: sipush          305
        //    79: if_icmple       2460
        //    82: iload_1         /* n */
        //    83: sipush          308
        //    86: if_icmplt       96
        //    89: iload_1         /* n */
        //    90: sipush          318
        //    93: if_icmple       2460
        //    96: iload_1         /* n */
        //    97: sipush          321
        //   100: if_icmplt       110
        //   103: iload_1         /* n */
        //   104: sipush          328
        //   107: if_icmple       2460
        //   110: iload_1         /* n */
        //   111: sipush          330
        //   114: if_icmplt       124
        //   117: iload_1         /* n */
        //   118: sipush          382
        //   121: if_icmple       2460
        //   124: iload_1         /* n */
        //   125: sipush          384
        //   128: if_icmplt       138
        //   131: iload_1         /* n */
        //   132: sipush          451
        //   135: if_icmple       2460
        //   138: iload_1         /* n */
        //   139: sipush          461
        //   142: if_icmplt       152
        //   145: iload_1         /* n */
        //   146: sipush          496
        //   149: if_icmple       2460
        //   152: iload_1         /* n */
        //   153: sipush          500
        //   156: if_icmplt       166
        //   159: iload_1         /* n */
        //   160: sipush          501
        //   163: if_icmple       2460
        //   166: iload_1         /* n */
        //   167: sipush          506
        //   170: if_icmplt       180
        //   173: iload_1         /* n */
        //   174: sipush          535
        //   177: if_icmple       2460
        //   180: iload_1         /* n */
        //   181: sipush          592
        //   184: if_icmplt       194
        //   187: iload_1         /* n */
        //   188: sipush          680
        //   191: if_icmple       2460
        //   194: iload_1         /* n */
        //   195: sipush          699
        //   198: if_icmplt       208
        //   201: iload_1         /* n */
        //   202: sipush          705
        //   205: if_icmple       2460
        //   208: iload_1         /* n */
        //   209: sipush          902
        //   212: if_icmpeq       2460
        //   215: iload_1         /* n */
        //   216: sipush          904
        //   219: if_icmplt       229
        //   222: iload_1         /* n */
        //   223: sipush          906
        //   226: if_icmple       2460
        //   229: iload_1         /* n */
        //   230: sipush          908
        //   233: if_icmpeq       2460
        //   236: iload_1         /* n */
        //   237: sipush          910
        //   240: if_icmplt       250
        //   243: iload_1         /* n */
        //   244: sipush          929
        //   247: if_icmple       2460
        //   250: iload_1         /* n */
        //   251: sipush          931
        //   254: if_icmplt       264
        //   257: iload_1         /* n */
        //   258: sipush          974
        //   261: if_icmple       2460
        //   264: iload_1         /* n */
        //   265: sipush          976
        //   268: if_icmplt       278
        //   271: iload_1         /* n */
        //   272: sipush          982
        //   275: if_icmple       2460
        //   278: iload_1         /* n */
        //   279: sipush          986
        //   282: if_icmpeq       2460
        //   285: iload_1         /* n */
        //   286: sipush          988
        //   289: if_icmpeq       2460
        //   292: iload_1         /* n */
        //   293: sipush          990
        //   296: if_icmpeq       2460
        //   299: iload_1         /* n */
        //   300: sipush          992
        //   303: if_icmpeq       2460
        //   306: iload_1         /* n */
        //   307: sipush          994
        //   310: if_icmplt       320
        //   313: iload_1         /* n */
        //   314: sipush          1011
        //   317: if_icmple       2460
        //   320: iload_1         /* n */
        //   321: sipush          1025
        //   324: if_icmplt       334
        //   327: iload_1         /* n */
        //   328: sipush          1036
        //   331: if_icmple       2460
        //   334: iload_1         /* n */
        //   335: sipush          1038
        //   338: if_icmplt       348
        //   341: iload_1         /* n */
        //   342: sipush          1103
        //   345: if_icmple       2460
        //   348: iload_1         /* n */
        //   349: sipush          1105
        //   352: if_icmplt       362
        //   355: iload_1         /* n */
        //   356: sipush          1116
        //   359: if_icmple       2460
        //   362: iload_1         /* n */
        //   363: sipush          1118
        //   366: if_icmplt       376
        //   369: iload_1         /* n */
        //   370: sipush          1153
        //   373: if_icmple       2460
        //   376: iload_1         /* n */
        //   377: sipush          1168
        //   380: if_icmplt       390
        //   383: iload_1         /* n */
        //   384: sipush          1220
        //   387: if_icmple       2460
        //   390: iload_1         /* n */
        //   391: sipush          1223
        //   394: if_icmplt       404
        //   397: iload_1         /* n */
        //   398: sipush          1224
        //   401: if_icmple       2460
        //   404: iload_1         /* n */
        //   405: sipush          1227
        //   408: if_icmplt       418
        //   411: iload_1         /* n */
        //   412: sipush          1228
        //   415: if_icmple       2460
        //   418: iload_1         /* n */
        //   419: sipush          1232
        //   422: if_icmplt       432
        //   425: iload_1         /* n */
        //   426: sipush          1259
        //   429: if_icmple       2460
        //   432: iload_1         /* n */
        //   433: sipush          1262
        //   436: if_icmplt       446
        //   439: iload_1         /* n */
        //   440: sipush          1269
        //   443: if_icmple       2460
        //   446: iload_1         /* n */
        //   447: sipush          1272
        //   450: if_icmplt       460
        //   453: iload_1         /* n */
        //   454: sipush          1273
        //   457: if_icmple       2460
        //   460: iload_1         /* n */
        //   461: sipush          1329
        //   464: if_icmplt       474
        //   467: iload_1         /* n */
        //   468: sipush          1366
        //   471: if_icmple       2460
        //   474: iload_1         /* n */
        //   475: sipush          1369
        //   478: if_icmpeq       2460
        //   481: iload_1         /* n */
        //   482: sipush          1377
        //   485: if_icmplt       495
        //   488: iload_1         /* n */
        //   489: sipush          1414
        //   492: if_icmple       2460
        //   495: iload_1         /* n */
        //   496: sipush          1488
        //   499: if_icmplt       509
        //   502: iload_1         /* n */
        //   503: sipush          1514
        //   506: if_icmple       2460
        //   509: iload_1         /* n */
        //   510: sipush          1520
        //   513: if_icmplt       523
        //   516: iload_1         /* n */
        //   517: sipush          1522
        //   520: if_icmple       2460
        //   523: iload_1         /* n */
        //   524: sipush          1569
        //   527: if_icmplt       537
        //   530: iload_1         /* n */
        //   531: sipush          1594
        //   534: if_icmple       2460
        //   537: iload_1         /* n */
        //   538: sipush          1601
        //   541: if_icmplt       551
        //   544: iload_1         /* n */
        //   545: sipush          1610
        //   548: if_icmple       2460
        //   551: iload_1         /* n */
        //   552: sipush          1649
        //   555: if_icmplt       565
        //   558: iload_1         /* n */
        //   559: sipush          1719
        //   562: if_icmple       2460
        //   565: iload_1         /* n */
        //   566: sipush          1722
        //   569: if_icmplt       579
        //   572: iload_1         /* n */
        //   573: sipush          1726
        //   576: if_icmple       2460
        //   579: iload_1         /* n */
        //   580: sipush          1728
        //   583: if_icmplt       593
        //   586: iload_1         /* n */
        //   587: sipush          1742
        //   590: if_icmple       2460
        //   593: iload_1         /* n */
        //   594: sipush          1744
        //   597: if_icmplt       607
        //   600: iload_1         /* n */
        //   601: sipush          1747
        //   604: if_icmple       2460
        //   607: iload_1         /* n */
        //   608: sipush          1749
        //   611: if_icmpeq       2460
        //   614: iload_1         /* n */
        //   615: sipush          1765
        //   618: if_icmplt       628
        //   621: iload_1         /* n */
        //   622: sipush          1766
        //   625: if_icmple       2460
        //   628: iload_1         /* n */
        //   629: sipush          2309
        //   632: if_icmplt       642
        //   635: iload_1         /* n */
        //   636: sipush          2361
        //   639: if_icmple       2460
        //   642: iload_1         /* n */
        //   643: sipush          2365
        //   646: if_icmpeq       2460
        //   649: iload_1         /* n */
        //   650: sipush          2392
        //   653: if_icmplt       663
        //   656: iload_1         /* n */
        //   657: sipush          2401
        //   660: if_icmple       2460
        //   663: iload_1         /* n */
        //   664: sipush          2437
        //   667: if_icmplt       677
        //   670: iload_1         /* n */
        //   671: sipush          2444
        //   674: if_icmple       2460
        //   677: iload_1         /* n */
        //   678: sipush          2447
        //   681: if_icmplt       691
        //   684: iload_1         /* n */
        //   685: sipush          2448
        //   688: if_icmple       2460
        //   691: iload_1         /* n */
        //   692: sipush          2451
        //   695: if_icmplt       705
        //   698: iload_1         /* n */
        //   699: sipush          2472
        //   702: if_icmple       2460
        //   705: iload_1         /* n */
        //   706: sipush          2474
        //   709: if_icmplt       719
        //   712: iload_1         /* n */
        //   713: sipush          2480
        //   716: if_icmple       2460
        //   719: iload_1         /* n */
        //   720: sipush          2482
        //   723: if_icmpeq       2460
        //   726: iload_1         /* n */
        //   727: sipush          2486
        //   730: if_icmplt       740
        //   733: iload_1         /* n */
        //   734: sipush          2489
        //   737: if_icmple       2460
        //   740: iload_1         /* n */
        //   741: sipush          2524
        //   744: if_icmplt       754
        //   747: iload_1         /* n */
        //   748: sipush          2525
        //   751: if_icmple       2460
        //   754: iload_1         /* n */
        //   755: sipush          2527
        //   758: if_icmplt       768
        //   761: iload_1         /* n */
        //   762: sipush          2529
        //   765: if_icmple       2460
        //   768: iload_1         /* n */
        //   769: sipush          2544
        //   772: if_icmplt       782
        //   775: iload_1         /* n */
        //   776: sipush          2545
        //   779: if_icmple       2460
        //   782: iload_1         /* n */
        //   783: sipush          2565
        //   786: if_icmplt       796
        //   789: iload_1         /* n */
        //   790: sipush          2570
        //   793: if_icmple       2460
        //   796: iload_1         /* n */
        //   797: sipush          2575
        //   800: if_icmplt       810
        //   803: iload_1         /* n */
        //   804: sipush          2576
        //   807: if_icmple       2460
        //   810: iload_1         /* n */
        //   811: sipush          2579
        //   814: if_icmplt       824
        //   817: iload_1         /* n */
        //   818: sipush          2600
        //   821: if_icmple       2460
        //   824: iload_1         /* n */
        //   825: sipush          2602
        //   828: if_icmplt       838
        //   831: iload_1         /* n */
        //   832: sipush          2608
        //   835: if_icmple       2460
        //   838: iload_1         /* n */
        //   839: sipush          2610
        //   842: if_icmplt       852
        //   845: iload_1         /* n */
        //   846: sipush          2611
        //   849: if_icmple       2460
        //   852: iload_1         /* n */
        //   853: sipush          2613
        //   856: if_icmplt       866
        //   859: iload_1         /* n */
        //   860: sipush          2614
        //   863: if_icmple       2460
        //   866: iload_1         /* n */
        //   867: sipush          2616
        //   870: if_icmplt       880
        //   873: iload_1         /* n */
        //   874: sipush          2617
        //   877: if_icmple       2460
        //   880: iload_1         /* n */
        //   881: sipush          2649
        //   884: if_icmplt       894
        //   887: iload_1         /* n */
        //   888: sipush          2652
        //   891: if_icmple       2460
        //   894: iload_1         /* n */
        //   895: sipush          2654
        //   898: if_icmpeq       2460
        //   901: iload_1         /* n */
        //   902: sipush          2674
        //   905: if_icmplt       915
        //   908: iload_1         /* n */
        //   909: sipush          2676
        //   912: if_icmple       2460
        //   915: iload_1         /* n */
        //   916: sipush          2693
        //   919: if_icmplt       929
        //   922: iload_1         /* n */
        //   923: sipush          2699
        //   926: if_icmple       2460
        //   929: iload_1         /* n */
        //   930: sipush          2701
        //   933: if_icmpeq       2460
        //   936: iload_1         /* n */
        //   937: sipush          2703
        //   940: if_icmplt       950
        //   943: iload_1         /* n */
        //   944: sipush          2705
        //   947: if_icmple       2460
        //   950: iload_1         /* n */
        //   951: sipush          2707
        //   954: if_icmplt       964
        //   957: iload_1         /* n */
        //   958: sipush          2728
        //   961: if_icmple       2460
        //   964: iload_1         /* n */
        //   965: sipush          2730
        //   968: if_icmplt       978
        //   971: iload_1         /* n */
        //   972: sipush          2736
        //   975: if_icmple       2460
        //   978: iload_1         /* n */
        //   979: sipush          2738
        //   982: if_icmplt       992
        //   985: iload_1         /* n */
        //   986: sipush          2739
        //   989: if_icmple       2460
        //   992: iload_1         /* n */
        //   993: sipush          2741
        //   996: if_icmplt       1006
        //   999: iload_1         /* n */
        //  1000: sipush          2745
        //  1003: if_icmple       2460
        //  1006: iload_1         /* n */
        //  1007: sipush          2749
        //  1010: if_icmpeq       2460
        //  1013: iload_1         /* n */
        //  1014: sipush          2784
        //  1017: if_icmpeq       2460
        //  1020: iload_1         /* n */
        //  1021: sipush          2821
        //  1024: if_icmplt       1034
        //  1027: iload_1         /* n */
        //  1028: sipush          2828
        //  1031: if_icmple       2460
        //  1034: iload_1         /* n */
        //  1035: sipush          2831
        //  1038: if_icmplt       1048
        //  1041: iload_1         /* n */
        //  1042: sipush          2832
        //  1045: if_icmple       2460
        //  1048: iload_1         /* n */
        //  1049: sipush          2835
        //  1052: if_icmplt       1062
        //  1055: iload_1         /* n */
        //  1056: sipush          2856
        //  1059: if_icmple       2460
        //  1062: iload_1         /* n */
        //  1063: sipush          2858
        //  1066: if_icmplt       1076
        //  1069: iload_1         /* n */
        //  1070: sipush          2864
        //  1073: if_icmple       2460
        //  1076: iload_1         /* n */
        //  1077: sipush          2866
        //  1080: if_icmplt       1090
        //  1083: iload_1         /* n */
        //  1084: sipush          2867
        //  1087: if_icmple       2460
        //  1090: iload_1         /* n */
        //  1091: sipush          2870
        //  1094: if_icmplt       1104
        //  1097: iload_1         /* n */
        //  1098: sipush          2873
        //  1101: if_icmple       2460
        //  1104: iload_1         /* n */
        //  1105: sipush          2877
        //  1108: if_icmpeq       2460
        //  1111: iload_1         /* n */
        //  1112: sipush          2908
        //  1115: if_icmplt       1125
        //  1118: iload_1         /* n */
        //  1119: sipush          2909
        //  1122: if_icmple       2460
        //  1125: iload_1         /* n */
        //  1126: sipush          2911
        //  1129: if_icmplt       1139
        //  1132: iload_1         /* n */
        //  1133: sipush          2913
        //  1136: if_icmple       2460
        //  1139: iload_1         /* n */
        //  1140: sipush          2949
        //  1143: if_icmplt       1153
        //  1146: iload_1         /* n */
        //  1147: sipush          2954
        //  1150: if_icmple       2460
        //  1153: iload_1         /* n */
        //  1154: sipush          2958
        //  1157: if_icmplt       1167
        //  1160: iload_1         /* n */
        //  1161: sipush          2960
        //  1164: if_icmple       2460
        //  1167: iload_1         /* n */
        //  1168: sipush          2962
        //  1171: if_icmplt       1181
        //  1174: iload_1         /* n */
        //  1175: sipush          2965
        //  1178: if_icmple       2460
        //  1181: iload_1         /* n */
        //  1182: sipush          2969
        //  1185: if_icmplt       1195
        //  1188: iload_1         /* n */
        //  1189: sipush          2970
        //  1192: if_icmple       2460
        //  1195: iload_1         /* n */
        //  1196: sipush          2972
        //  1199: if_icmpeq       2460
        //  1202: iload_1         /* n */
        //  1203: sipush          2974
        //  1206: if_icmplt       1216
        //  1209: iload_1         /* n */
        //  1210: sipush          2975
        //  1213: if_icmple       2460
        //  1216: iload_1         /* n */
        //  1217: sipush          2979
        //  1220: if_icmplt       1230
        //  1223: iload_1         /* n */
        //  1224: sipush          2980
        //  1227: if_icmple       2460
        //  1230: iload_1         /* n */
        //  1231: sipush          2984
        //  1234: if_icmplt       1244
        //  1237: iload_1         /* n */
        //  1238: sipush          2986
        //  1241: if_icmple       2460
        //  1244: iload_1         /* n */
        //  1245: sipush          2990
        //  1248: if_icmplt       1258
        //  1251: iload_1         /* n */
        //  1252: sipush          2997
        //  1255: if_icmple       2460
        //  1258: iload_1         /* n */
        //  1259: sipush          2999
        //  1262: if_icmplt       1272
        //  1265: iload_1         /* n */
        //  1266: sipush          3001
        //  1269: if_icmple       2460
        //  1272: iload_1         /* n */
        //  1273: sipush          3077
        //  1276: if_icmplt       1286
        //  1279: iload_1         /* n */
        //  1280: sipush          3084
        //  1283: if_icmple       2460
        //  1286: iload_1         /* n */
        //  1287: sipush          3086
        //  1290: if_icmplt       1300
        //  1293: iload_1         /* n */
        //  1294: sipush          3088
        //  1297: if_icmple       2460
        //  1300: iload_1         /* n */
        //  1301: sipush          3090
        //  1304: if_icmplt       1314
        //  1307: iload_1         /* n */
        //  1308: sipush          3112
        //  1311: if_icmple       2460
        //  1314: iload_1         /* n */
        //  1315: sipush          3114
        //  1318: if_icmplt       1328
        //  1321: iload_1         /* n */
        //  1322: sipush          3123
        //  1325: if_icmple       2460
        //  1328: iload_1         /* n */
        //  1329: sipush          3125
        //  1332: if_icmplt       1342
        //  1335: iload_1         /* n */
        //  1336: sipush          3129
        //  1339: if_icmple       2460
        //  1342: iload_1         /* n */
        //  1343: sipush          3168
        //  1346: if_icmplt       1356
        //  1349: iload_1         /* n */
        //  1350: sipush          3169
        //  1353: if_icmple       2460
        //  1356: iload_1         /* n */
        //  1357: sipush          3205
        //  1360: if_icmplt       1370
        //  1363: iload_1         /* n */
        //  1364: sipush          3212
        //  1367: if_icmple       2460
        //  1370: iload_1         /* n */
        //  1371: sipush          3214
        //  1374: if_icmplt       1384
        //  1377: iload_1         /* n */
        //  1378: sipush          3216
        //  1381: if_icmple       2460
        //  1384: iload_1         /* n */
        //  1385: sipush          3218
        //  1388: if_icmplt       1398
        //  1391: iload_1         /* n */
        //  1392: sipush          3240
        //  1395: if_icmple       2460
        //  1398: iload_1         /* n */
        //  1399: sipush          3242
        //  1402: if_icmplt       1412
        //  1405: iload_1         /* n */
        //  1406: sipush          3251
        //  1409: if_icmple       2460
        //  1412: iload_1         /* n */
        //  1413: sipush          3253
        //  1416: if_icmplt       1426
        //  1419: iload_1         /* n */
        //  1420: sipush          3257
        //  1423: if_icmple       2460
        //  1426: iload_1         /* n */
        //  1427: sipush          3294
        //  1430: if_icmpeq       2460
        //  1433: iload_1         /* n */
        //  1434: sipush          3296
        //  1437: if_icmplt       1447
        //  1440: iload_1         /* n */
        //  1441: sipush          3297
        //  1444: if_icmple       2460
        //  1447: iload_1         /* n */
        //  1448: sipush          3333
        //  1451: if_icmplt       1461
        //  1454: iload_1         /* n */
        //  1455: sipush          3340
        //  1458: if_icmple       2460
        //  1461: iload_1         /* n */
        //  1462: sipush          3342
        //  1465: if_icmplt       1475
        //  1468: iload_1         /* n */
        //  1469: sipush          3344
        //  1472: if_icmple       2460
        //  1475: iload_1         /* n */
        //  1476: sipush          3346
        //  1479: if_icmplt       1489
        //  1482: iload_1         /* n */
        //  1483: sipush          3368
        //  1486: if_icmple       2460
        //  1489: iload_1         /* n */
        //  1490: sipush          3370
        //  1493: if_icmplt       1503
        //  1496: iload_1         /* n */
        //  1497: sipush          3385
        //  1500: if_icmple       2460
        //  1503: iload_1         /* n */
        //  1504: sipush          3424
        //  1507: if_icmplt       1517
        //  1510: iload_1         /* n */
        //  1511: sipush          3425
        //  1514: if_icmple       2460
        //  1517: iload_1         /* n */
        //  1518: sipush          3585
        //  1521: if_icmplt       1531
        //  1524: iload_1         /* n */
        //  1525: sipush          3630
        //  1528: if_icmple       2460
        //  1531: iload_1         /* n */
        //  1532: sipush          3632
        //  1535: if_icmpeq       2460
        //  1538: iload_1         /* n */
        //  1539: sipush          3634
        //  1542: if_icmplt       1552
        //  1545: iload_1         /* n */
        //  1546: sipush          3635
        //  1549: if_icmple       2460
        //  1552: iload_1         /* n */
        //  1553: sipush          3648
        //  1556: if_icmplt       1566
        //  1559: iload_1         /* n */
        //  1560: sipush          3653
        //  1563: if_icmple       2460
        //  1566: iload_1         /* n */
        //  1567: sipush          3713
        //  1570: if_icmplt       1580
        //  1573: iload_1         /* n */
        //  1574: sipush          3714
        //  1577: if_icmple       2460
        //  1580: iload_1         /* n */
        //  1581: sipush          3716
        //  1584: if_icmpeq       2460
        //  1587: iload_1         /* n */
        //  1588: sipush          3719
        //  1591: if_icmplt       1601
        //  1594: iload_1         /* n */
        //  1595: sipush          3720
        //  1598: if_icmple       2460
        //  1601: iload_1         /* n */
        //  1602: sipush          3722
        //  1605: if_icmpeq       2460
        //  1608: iload_1         /* n */
        //  1609: sipush          3725
        //  1612: if_icmpeq       2460
        //  1615: iload_1         /* n */
        //  1616: sipush          3732
        //  1619: if_icmplt       1629
        //  1622: iload_1         /* n */
        //  1623: sipush          3735
        //  1626: if_icmple       2460
        //  1629: iload_1         /* n */
        //  1630: sipush          3737
        //  1633: if_icmplt       1643
        //  1636: iload_1         /* n */
        //  1637: sipush          3743
        //  1640: if_icmple       2460
        //  1643: iload_1         /* n */
        //  1644: sipush          3745
        //  1647: if_icmplt       1657
        //  1650: iload_1         /* n */
        //  1651: sipush          3747
        //  1654: if_icmple       2460
        //  1657: iload_1         /* n */
        //  1658: sipush          3749
        //  1661: if_icmpeq       2460
        //  1664: iload_1         /* n */
        //  1665: sipush          3751
        //  1668: if_icmpeq       2460
        //  1671: iload_1         /* n */
        //  1672: sipush          3754
        //  1675: if_icmplt       1685
        //  1678: iload_1         /* n */
        //  1679: sipush          3755
        //  1682: if_icmple       2460
        //  1685: iload_1         /* n */
        //  1686: sipush          3757
        //  1689: if_icmplt       1699
        //  1692: iload_1         /* n */
        //  1693: sipush          3758
        //  1696: if_icmple       2460
        //  1699: iload_1         /* n */
        //  1700: sipush          3760
        //  1703: if_icmpeq       2460
        //  1706: iload_1         /* n */
        //  1707: sipush          3762
        //  1710: if_icmplt       1720
        //  1713: iload_1         /* n */
        //  1714: sipush          3763
        //  1717: if_icmple       2460
        //  1720: iload_1         /* n */
        //  1721: sipush          3773
        //  1724: if_icmpeq       2460
        //  1727: iload_1         /* n */
        //  1728: sipush          3776
        //  1731: if_icmplt       1741
        //  1734: iload_1         /* n */
        //  1735: sipush          3780
        //  1738: if_icmple       2460
        //  1741: iload_1         /* n */
        //  1742: sipush          3904
        //  1745: if_icmplt       1755
        //  1748: iload_1         /* n */
        //  1749: sipush          3911
        //  1752: if_icmple       2460
        //  1755: iload_1         /* n */
        //  1756: sipush          3913
        //  1759: if_icmplt       1769
        //  1762: iload_1         /* n */
        //  1763: sipush          3945
        //  1766: if_icmple       2460
        //  1769: iload_1         /* n */
        //  1770: sipush          4256
        //  1773: if_icmplt       1783
        //  1776: iload_1         /* n */
        //  1777: sipush          4293
        //  1780: if_icmple       2460
        //  1783: iload_1         /* n */
        //  1784: sipush          4304
        //  1787: if_icmplt       1797
        //  1790: iload_1         /* n */
        //  1791: sipush          4342
        //  1794: if_icmple       2460
        //  1797: iload_1         /* n */
        //  1798: sipush          4352
        //  1801: if_icmpeq       2460
        //  1804: iload_1         /* n */
        //  1805: sipush          4354
        //  1808: if_icmplt       1818
        //  1811: iload_1         /* n */
        //  1812: sipush          4355
        //  1815: if_icmple       2460
        //  1818: iload_1         /* n */
        //  1819: sipush          4357
        //  1822: if_icmplt       1832
        //  1825: iload_1         /* n */
        //  1826: sipush          4359
        //  1829: if_icmple       2460
        //  1832: iload_1         /* n */
        //  1833: sipush          4361
        //  1836: if_icmpeq       2460
        //  1839: iload_1         /* n */
        //  1840: sipush          4363
        //  1843: if_icmplt       1853
        //  1846: iload_1         /* n */
        //  1847: sipush          4364
        //  1850: if_icmple       2460
        //  1853: iload_1         /* n */
        //  1854: sipush          4366
        //  1857: if_icmplt       1867
        //  1860: iload_1         /* n */
        //  1861: sipush          4370
        //  1864: if_icmple       2460
        //  1867: iload_1         /* n */
        //  1868: sipush          4412
        //  1871: if_icmpeq       2460
        //  1874: iload_1         /* n */
        //  1875: sipush          4414
        //  1878: if_icmpeq       2460
        //  1881: iload_1         /* n */
        //  1882: sipush          4416
        //  1885: if_icmpeq       2460
        //  1888: iload_1         /* n */
        //  1889: sipush          4428
        //  1892: if_icmpeq       2460
        //  1895: iload_1         /* n */
        //  1896: sipush          4430
        //  1899: if_icmpeq       2460
        //  1902: iload_1         /* n */
        //  1903: sipush          4432
        //  1906: if_icmpeq       2460
        //  1909: iload_1         /* n */
        //  1910: sipush          4436
        //  1913: if_icmplt       1923
        //  1916: iload_1         /* n */
        //  1917: sipush          4437
        //  1920: if_icmple       2460
        //  1923: iload_1         /* n */
        //  1924: sipush          4441
        //  1927: if_icmpeq       2460
        //  1930: iload_1         /* n */
        //  1931: sipush          4447
        //  1934: if_icmplt       1944
        //  1937: iload_1         /* n */
        //  1938: sipush          4449
        //  1941: if_icmple       2460
        //  1944: iload_1         /* n */
        //  1945: sipush          4451
        //  1948: if_icmpeq       2460
        //  1951: iload_1         /* n */
        //  1952: sipush          4453
        //  1955: if_icmpeq       2460
        //  1958: iload_1         /* n */
        //  1959: sipush          4455
        //  1962: if_icmpeq       2460
        //  1965: iload_1         /* n */
        //  1966: sipush          4457
        //  1969: if_icmpeq       2460
        //  1972: iload_1         /* n */
        //  1973: sipush          4461
        //  1976: if_icmplt       1986
        //  1979: iload_1         /* n */
        //  1980: sipush          4462
        //  1983: if_icmple       2460
        //  1986: iload_1         /* n */
        //  1987: sipush          4466
        //  1990: if_icmplt       2000
        //  1993: iload_1         /* n */
        //  1994: sipush          4467
        //  1997: if_icmple       2460
        //  2000: iload_1         /* n */
        //  2001: sipush          4469
        //  2004: if_icmpeq       2460
        //  2007: iload_1         /* n */
        //  2008: sipush          4510
        //  2011: if_icmpeq       2460
        //  2014: iload_1         /* n */
        //  2015: sipush          4520
        //  2018: if_icmpeq       2460
        //  2021: iload_1         /* n */
        //  2022: sipush          4523
        //  2025: if_icmpeq       2460
        //  2028: iload_1         /* n */
        //  2029: sipush          4526
        //  2032: if_icmplt       2042
        //  2035: iload_1         /* n */
        //  2036: sipush          4527
        //  2039: if_icmple       2460
        //  2042: iload_1         /* n */
        //  2043: sipush          4535
        //  2046: if_icmplt       2056
        //  2049: iload_1         /* n */
        //  2050: sipush          4536
        //  2053: if_icmple       2460
        //  2056: iload_1         /* n */
        //  2057: sipush          4538
        //  2060: if_icmpeq       2460
        //  2063: iload_1         /* n */
        //  2064: sipush          4540
        //  2067: if_icmplt       2077
        //  2070: iload_1         /* n */
        //  2071: sipush          4546
        //  2074: if_icmple       2460
        //  2077: iload_1         /* n */
        //  2078: sipush          4587
        //  2081: if_icmpeq       2460
        //  2084: iload_1         /* n */
        //  2085: sipush          4592
        //  2088: if_icmpeq       2460
        //  2091: iload_1         /* n */
        //  2092: sipush          4601
        //  2095: if_icmpeq       2460
        //  2098: iload_1         /* n */
        //  2099: sipush          7680
        //  2102: if_icmplt       2112
        //  2105: iload_1         /* n */
        //  2106: sipush          7835
        //  2109: if_icmple       2460
        //  2112: iload_1         /* n */
        //  2113: sipush          7840
        //  2116: if_icmplt       2126
        //  2119: iload_1         /* n */
        //  2120: sipush          7929
        //  2123: if_icmple       2460
        //  2126: iload_1         /* n */
        //  2127: sipush          7936
        //  2130: if_icmplt       2140
        //  2133: iload_1         /* n */
        //  2134: sipush          7957
        //  2137: if_icmple       2460
        //  2140: iload_1         /* n */
        //  2141: sipush          7960
        //  2144: if_icmplt       2154
        //  2147: iload_1         /* n */
        //  2148: sipush          7965
        //  2151: if_icmple       2460
        //  2154: iload_1         /* n */
        //  2155: sipush          7968
        //  2158: if_icmplt       2168
        //  2161: iload_1         /* n */
        //  2162: sipush          8005
        //  2165: if_icmple       2460
        //  2168: iload_1         /* n */
        //  2169: sipush          8008
        //  2172: if_icmplt       2182
        //  2175: iload_1         /* n */
        //  2176: sipush          8013
        //  2179: if_icmple       2460
        //  2182: iload_1         /* n */
        //  2183: sipush          8016
        //  2186: if_icmplt       2196
        //  2189: iload_1         /* n */
        //  2190: sipush          8023
        //  2193: if_icmple       2460
        //  2196: iload_1         /* n */
        //  2197: sipush          8025
        //  2200: if_icmpeq       2460
        //  2203: iload_1         /* n */
        //  2204: sipush          8027
        //  2207: if_icmpeq       2460
        //  2210: iload_1         /* n */
        //  2211: sipush          8029
        //  2214: if_icmpeq       2460
        //  2217: iload_1         /* n */
        //  2218: sipush          8031
        //  2221: if_icmplt       2231
        //  2224: iload_1         /* n */
        //  2225: sipush          8061
        //  2228: if_icmple       2460
        //  2231: iload_1         /* n */
        //  2232: sipush          8064
        //  2235: if_icmplt       2245
        //  2238: iload_1         /* n */
        //  2239: sipush          8116
        //  2242: if_icmple       2460
        //  2245: iload_1         /* n */
        //  2246: sipush          8118
        //  2249: if_icmplt       2259
        //  2252: iload_1         /* n */
        //  2253: sipush          8124
        //  2256: if_icmple       2460
        //  2259: iload_1         /* n */
        //  2260: sipush          8126
        //  2263: if_icmpeq       2460
        //  2266: iload_1         /* n */
        //  2267: sipush          8130
        //  2270: if_icmplt       2280
        //  2273: iload_1         /* n */
        //  2274: sipush          8132
        //  2277: if_icmple       2460
        //  2280: iload_1         /* n */
        //  2281: sipush          8134
        //  2284: if_icmplt       2294
        //  2287: iload_1         /* n */
        //  2288: sipush          8140
        //  2291: if_icmple       2460
        //  2294: iload_1         /* n */
        //  2295: sipush          8144
        //  2298: if_icmplt       2308
        //  2301: iload_1         /* n */
        //  2302: sipush          8147
        //  2305: if_icmple       2460
        //  2308: iload_1         /* n */
        //  2309: sipush          8150
        //  2312: if_icmplt       2322
        //  2315: iload_1         /* n */
        //  2316: sipush          8155
        //  2319: if_icmple       2460
        //  2322: iload_1         /* n */
        //  2323: sipush          8160
        //  2326: if_icmplt       2336
        //  2329: iload_1         /* n */
        //  2330: sipush          8172
        //  2333: if_icmple       2460
        //  2336: iload_1         /* n */
        //  2337: sipush          8178
        //  2340: if_icmplt       2350
        //  2343: iload_1         /* n */
        //  2344: sipush          8180
        //  2347: if_icmple       2460
        //  2350: iload_1         /* n */
        //  2351: sipush          8182
        //  2354: if_icmplt       2364
        //  2357: iload_1         /* n */
        //  2358: sipush          8188
        //  2361: if_icmple       2460
        //  2364: iload_1         /* n */
        //  2365: sipush          8486
        //  2368: if_icmpeq       2460
        //  2371: iload_1         /* n */
        //  2372: sipush          8490
        //  2375: if_icmplt       2385
        //  2378: iload_1         /* n */
        //  2379: sipush          8491
        //  2382: if_icmple       2460
        //  2385: iload_1         /* n */
        //  2386: sipush          8494
        //  2389: if_icmpeq       2460
        //  2392: iload_1         /* n */
        //  2393: sipush          8576
        //  2396: if_icmplt       2406
        //  2399: iload_1         /* n */
        //  2400: sipush          8578
        //  2403: if_icmple       2460
        //  2406: iload_1         /* n */
        //  2407: sipush          12353
        //  2410: if_icmplt       2420
        //  2413: iload_1         /* n */
        //  2414: sipush          12436
        //  2417: if_icmple       2460
        //  2420: iload_1         /* n */
        //  2421: sipush          12449
        //  2424: if_icmplt       2434
        //  2427: iload_1         /* n */
        //  2428: sipush          12538
        //  2431: if_icmple       2460
        //  2434: iload_1         /* n */
        //  2435: sipush          12549
        //  2438: if_icmplt       2448
        //  2441: iload_1         /* n */
        //  2442: sipush          12588
        //  2445: if_icmple       2460
        //  2448: iload_1         /* n */
        //  2449: ldc             44032
        //  2451: if_icmplt       2464
        //  2454: iload_1         /* n */
        //  2455: ldc             55203
        //  2457: if_icmpgt       2464
        //  2460: iconst_1       
        //  2461: goto            2465
        //  2464: iconst_0       
        //  2465: ireturn        
        // 
        // The error that occurred was:
        // 
        // java.lang.StackOverflowError
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.DecompilerLinkProvider.generateContent(DecompilerLinkProvider.java:97)
        //     at us.deathmarine.luyten.OpenFile.decompileWithNavigationLinks(OpenFile.java:469)
        //     at us.deathmarine.luyten.OpenFile.decompile(OpenFile.java:442)
        //     at us.deathmarine.luyten.Model.extractClassToTextPane(Model.java:420)
        //     at us.deathmarine.luyten.Model.openEntryByTreePath(Model.java:339)
        //     at us.deathmarine.luyten.Model$TreeListener$1.run(Model.java:266)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static final boolean isIdeographic(final char c) {
        return (c >= '\u4e00' && c <= '\u9fa5') || c == '\u3007' || (c >= '\u3021' && c <= '\u3029');
    }
    
    private static final boolean isCombiningChar(final char c) {
        return (c >= '\u0300' && c <= '\u0345') || (c >= '\u0360' && c <= '\u0361') || (c >= '\u0483' && c <= '\u0486') || (c >= '\u0591' && c <= '\u05a1') || (c >= '\u05a3' && c <= '\u05b9') || (c >= '\u05bb' && c <= '\u05bd') || c == '\u05bf' || (c >= '\u05c1' && c <= '\u05c2') || c == '\u05c4' || (c >= '\u064b' && c <= '\u0652') || c == '\u0670' || (c >= '\u06d6' && c <= '\u06dc') || (c >= '\u06dd' && c <= '\u06df') || (c >= '\u06e0' && c <= '\u06e4') || (c >= '\u06e7' && c <= '\u06e8') || (c >= '\u06ea' && c <= '\u06ed') || (c >= '\u0901' && c <= '\u0903') || c == '\u093c' || (c >= '\u093e' && c <= '\u094c') || c == '\u094d' || (c >= '\u0951' && c <= '\u0954') || (c >= '\u0962' && c <= '\u0963') || (c >= '\u0981' && c <= '\u0983') || c == '\u09bc' || c == '\u09be' || c == '\u09bf' || (c >= '\u09c0' && c <= '\u09c4') || (c >= '\u09c7' && c <= '\u09c8') || (c >= '\u09cb' && c <= '\u09cd') || c == '\u09d7' || (c >= '\u09e2' && c <= '\u09e3') || c == '\u0a02' || c == '\u0a3c' || c == '\u0a3e' || c == '\u0a3f' || (c >= '\u0a40' && c <= '\u0a42') || (c >= '\u0a47' && c <= '\u0a48') || (c >= '\u0a4b' && c <= '\u0a4d') || (c >= '\u0a70' && c <= '\u0a71') || (c >= '\u0a81' && c <= '\u0a83') || c == '\u0abc' || (c >= '\u0abe' && c <= '\u0ac5') || (c >= '\u0ac7' && c <= '\u0ac9') || (c >= '\u0acb' && c <= '\u0acd') || (c >= '\u0b01' && c <= '\u0b03') || c == '\u0b3c' || (c >= '\u0b3e' && c <= '\u0b43') || (c >= '\u0b47' && c <= '\u0b48') || (c >= '\u0b4b' && c <= '\u0b4d') || (c >= '\u0b56' && c <= '\u0b57') || (c >= '\u0b82' && c <= '\u0b83') || (c >= '\u0bbe' && c <= '\u0bc2') || (c >= '\u0bc6' && c <= '\u0bc8') || (c >= '\u0bca' && c <= '\u0bcd') || c == '\u0bd7' || (c >= '\u0c01' && c <= '\u0c03') || (c >= '\u0c3e' && c <= '\u0c44') || (c >= '\u0c46' && c <= '\u0c48') || (c >= '\u0c4a' && c <= '\u0c4d') || (c >= '\u0c55' && c <= '\u0c56') || (c >= '\u0c82' && c <= '\u0c83') || (c >= '\u0cbe' && c <= '\u0cc4') || (c >= '\u0cc6' && c <= '\u0cc8') || (c >= '\u0cca' && c <= '\u0ccd') || (c >= '\u0cd5' && c <= '\u0cd6') || (c >= '\u0d02' && c <= '\u0d03') || (c >= '\u0d3e' && c <= '\u0d43') || (c >= '\u0d46' && c <= '\u0d48') || (c >= '\u0d4a' && c <= '\u0d4d') || c == '\u0d57' || c == '\u0e31' || (c >= '\u0e34' && c <= '\u0e3a') || (c >= '\u0e47' && c <= '\u0e4e') || c == '\u0eb1' || (c >= '\u0eb4' && c <= '\u0eb9') || (c >= '\u0ebb' && c <= '\u0ebc') || (c >= '\u0ec8' && c <= '\u0ecd') || (c >= '\u0f18' && c <= '\u0f19') || c == '\u0f35' || c == '\u0f37' || c == '\u0f39' || c == '\u0f3e' || c == '\u0f3f' || (c >= '\u0f71' && c <= '\u0f84') || (c >= '\u0f86' && c <= '\u0f8b') || (c >= '\u0f90' && c <= '\u0f95') || c == '\u0f97' || (c >= '\u0f99' && c <= '\u0fad') || (c >= '\u0fb1' && c <= '\u0fb7') || c == '\u0fb9' || (c >= '\u20d0' && c <= '\u20dc') || c == '\u20e1' || (c >= '\u302a' && c <= '\u302f') || c == '\u3099' || c == '\u309a';
    }
    
    private static final boolean isDigit(final char c) {
        return (c >= '0' && c <= '9') || (c >= '\u0660' && c <= '\u0669') || (c >= '\u06f0' && c <= '\u06f9') || (c >= '\u0966' && c <= '\u096f') || (c >= '\u09e6' && c <= '\u09ef') || (c >= '\u0a66' && c <= '\u0a6f') || (c >= '\u0ae6' && c <= '\u0aef') || (c >= '\u0b66' && c <= '\u0b6f') || (c >= '\u0be7' && c <= '\u0bef') || (c >= '\u0c66' && c <= '\u0c6f') || (c >= '\u0ce6' && c <= '\u0cef') || (c >= '\u0d66' && c <= '\u0d6f') || (c >= '\u0e50' && c <= '\u0e59') || (c >= '\u0ed0' && c <= '\u0ed9') || (c >= '\u0f20' && c <= '\u0f29');
    }
    
    private static final boolean isExtender(final char c) {
        return c == '·' || c == '\u02d0' || c == '\u02d1' || c == '\u0387' || c == '\u0640' || c == '\u0e46' || c == '\u0ec6' || c == '\u3005' || (c >= '\u3031' && c <= '\u3035') || (c >= '\u309d' && c <= '\u309e') || (c >= '\u30fc' && c <= '\u30fe');
    }
}
